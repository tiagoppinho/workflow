/*
 * @(#)RevisionRenderer.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Meta-Workflow Module.
 *
 *   The Meta-Workflow Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Meta-Workflow Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Meta-Workflow Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.metaWorkflow.presentationTier.renderers;

import java.util.ArrayList;
import java.util.List;

import module.metaWorkflow.util.versioning.DiffUtil.Revision;

import org.apache.commons.jrcs.diff.Chunk;
import org.apache.commons.jrcs.diff.Delta;
import org.apache.commons.jrcs.diff.Diff;
import org.apache.commons.jrcs.util.ToString;
import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

/**
 * 
 * The code of this Rendered is basically the adaption of
 * com.xpn.xwiki.plugin.diff.DiffPlugin from XWiki which has a kick ass diff
 * HTML display algorithm!
 * 
 * Base code can be found in: https://svn.xwiki.org/svnroot/xwiki/platform
 * /core/trunk/xwiki-core/src
 * /main/java/com/xpn/xwiki/plugin/diff/DiffPlugin.java
 * 
 * @author Paulo Abrantes
 * 
 */
public class RevisionRenderer extends OutputRenderer {

    private String modifiedLineClass;
    private String unmodifiedline;
    private String removeWord;
    private String addWord;
    private boolean allDoc;

    public boolean isAllDoc() {
        return allDoc;
    }

    public void setAllDoc(boolean allDoc) {
        this.allDoc = allDoc;
    }

    public String getModifiedLineClass() {
        return modifiedLineClass;
    }

    public void setModifiedLineClass(String modifiedLineClass) {
        this.modifiedLineClass = modifiedLineClass;
    }

    public String getUnmodifiedline() {
        return unmodifiedline;
    }

    public void setUnmodifiedline(String unmodifiedline) {
        this.unmodifiedline = unmodifiedline;
    }

    public String getRemoveWord() {
        return removeWord;
    }

    public void setRemoveWord(String removeword) {
        this.removeWord = removeword;
    }

    public String getAddWord() {
        return addWord;
    }

    public void setAddWord(String addWord) {
        this.addWord = addWord;
    }

    @Override
    protected Layout getLayout(Object arg0, Class arg1) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object arg0, Class arg1) {
                Revision revision = (Revision) arg0;
                HtmlBlockContainer container = new HtmlBlockContainer();

                List<Delta> deltaList = getDeltas(revision.getRevision());
                String[] lines = ToString.stringToArray(revision.getText1());
                int cursor = 0;
                boolean addBR = false;

                for (int i = 0; i < deltaList.size(); i++) {
                    if (addBR) {
                        addBR = false;
                    }

                    Delta delta = deltaList.get(i);
                    int position = delta.getOriginal().anchor();
                    // First we fill in all text that has not been changed
                    while (cursor < position) {
                        if (isAllDoc()) {
                            HtmlBlockContainer unmodifiedContainer = new HtmlBlockContainer();
                            container.addChild(unmodifiedContainer);
                            unmodifiedContainer.setClasses(getUnmodifiedline());
                            String text = escape(lines[cursor]);
                            if (text.equals("")) {
                                text = " ";
                            }
                            unmodifiedContainer.addChild(new HtmlText(text));
                        }
                        cursor++;
                    }

                    // Then we fill in what has been removed
                    Chunk orig = delta.getOriginal();
                    Chunk rev = delta.getRevised();
                    int j1 = 0;

                    if (orig.size() > 0) {
                        List<String> chunks = orig.chunk();
                        int j2 = 0;
                        for (int j = 0; j < chunks.size(); j++) {
                            String origline = chunks.get(j);
                            if (origline.equals("")) {
                                cursor++;
                                continue;
                            }
                            // if (j>0)
                            // html.append("<br/>");
                            List<String> revchunks = rev.chunk();
                            String revline = "";
                            while ("".equals(revline)) {
                                revline = (j2 >= revchunks.size()) ? null : revchunks.get(j2);
                                j2++;
                                j1++;
                            }
                            if (revline != null) {
                                container.addChild(getWordDifference(origline, revline));
                            } else {
                                HtmlBlockContainer modifiedLine = new HtmlBlockContainer();
                                container.addChild(modifiedLine);
                                modifiedLine.setClasses(getModifiedLineClass());
                                HtmlInlineContainer modifiedWord = new HtmlInlineContainer();
                                modifiedLine.addChild(modifiedWord);
                                modifiedLine.setClasses(getRemoveWord());
                                modifiedLine.addChild(new HtmlText(origline));
                            }
                            addBR = true;
                            cursor++;
                        }
                    }

                    // Then we fill in what has been added
                    if (rev.size() > 0) {
                        List<String> chunks = rev.chunk();
                        for (int j = j1; j < chunks.size(); j++) {
                            HtmlBlockContainer modifiedLine = new HtmlBlockContainer();
                            container.addChild(modifiedLine);
                            modifiedLine.setClasses(getModifiedLineClass());
                            HtmlInlineContainer modifiedWord = new HtmlInlineContainer();
                            modifiedLine.addChild(modifiedWord);
                            modifiedLine.setClasses(getAddWord());
                            modifiedLine.addChild(new HtmlText(chunks.get(j)));
                        }
                        addBR = true;
                    }
                }

                // First we fill in all text that has not been changed
                if (isAllDoc()) {
                    while (cursor < lines.length) {
                        HtmlBlockContainer unmodifiedLine = new HtmlBlockContainer();
                        container.addChild(unmodifiedLine);
                        unmodifiedLine.setClasses(getUnmodifiedline());
                        String text = escape(lines[cursor]);
                        if (text.equals("")) {
                            text = " ";
                        }
                        unmodifiedLine.addChild(new HtmlText(text));
                        cursor++;
                    }
                }

                return container;
            }

            private HtmlContainer getWordDifference(String text1, String text2) {
                text1 = "~~PLACEHOLDER~~" + text1 + "~~PLACEHOLDER~~";
                text2 = "~~PLACEHOLDER~~" + text2 + "~~PLACEHOLDER~~";

                HtmlBlockContainer container = new HtmlBlockContainer();
                container.setClasses(getModifiedLineClass());
                List<Delta> list = getWordDifferencesAsList(text1, text2);
                String[] words = StringUtils.splitPreserveAllTokens(text1, ' ');
                int cursor = 0;
                boolean addSpace = false;

                for (int i = 0; i < list.size(); i++) {
                    if (addSpace) {
                        container.addChild(new HtmlText(" "));
                        addSpace = false;
                    }

                    Delta delta = list.get(i);
                    int position = delta.getOriginal().anchor();
                    // First we fill in all text that has not been changed
                    while (cursor < position) {
                        container.addChild(new HtmlText(escape(words[cursor])));
                        container.addChild(new HtmlText(" "));
                        cursor++;
                    }
                    // Then we fill in what has been removed
                    Chunk orig = delta.getOriginal();
                    if (orig.size() > 0) {
                        HtmlInlineContainer removeWord = new HtmlInlineContainer();
                        container.addChild(removeWord);
                        removeWord.setClasses(getRemoveWord());
                        List<String> chunks = orig.chunk();

                        for (int j = 0; j < chunks.size(); j++) {
                            if (j > 0) {
                                removeWord.addChild(new HtmlText(" "));
                            }
                            removeWord.addChild(new HtmlText(escape(chunks.get(j))));
                            cursor++;
                        }
                        addSpace = true;
                    }

                    // Then we fill in what has been added
                    Chunk rev = delta.getRevised();
                    if (rev.size() > 0) {
                        HtmlInlineContainer addWord = new HtmlInlineContainer();
                        container.addChild(addWord);
                        addWord.setClasses(getAddWord());

                        List<String> chunks = rev.chunk();
                        for (int j = 0; j < chunks.size(); j++) {
                            if (j > 0) {
                                addWord.addChild(new HtmlText(" "));
                            }
                            addWord.addChild(new HtmlText(escape(chunks.get(j))));
                        }
                        addSpace = true;
                    }
                }

                // First we fill in all text that has not been changed
                while (cursor < words.length) {
                    if (addSpace) {
                        container.addChild(new HtmlText(" "));
                    }
                    container.addChild(new HtmlText(escape(words[cursor])));
                    addSpace = true;
                    cursor++;
                }

                return container;
                // return html.toString().replaceAll("~~PLACEHOLDER~~", "");
            }

            private List<Delta> getWordDifferencesAsList(String text1, String text2) {
                try {
                    text1 = text1.replaceAll(" ", "\n");
                    text2 = text2.replaceAll(" ", "\n");
                    return getDeltas(Diff.diff(ToString.stringToArray(text1), ToString.stringToArray(text2)));
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            private List<Delta> getDeltas(org.apache.commons.jrcs.diff.Revision revision) {
                ArrayList<Delta> deltas = new ArrayList<Delta>();
                for (int i = 0; i < revision.size(); i++) {
                    deltas.add(revision.getDelta(i));
                }
                return deltas;
            }

            private String escape(String string) {
                return string.replaceAll("~~PLACEHOLDER~~", "");
            }

        };
    }

}
