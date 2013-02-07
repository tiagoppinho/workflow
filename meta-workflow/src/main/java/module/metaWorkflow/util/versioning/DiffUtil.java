/*
 * @(#)DiffUtil.java
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
package module.metaWorkflow.util.versioning;

import java.io.Serializable;

import org.apache.commons.jrcs.diff.Diff;
import org.apache.commons.jrcs.diff.DifferentiationFailedException;
import org.apache.commons.jrcs.util.ToString;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class DiffUtil implements Serializable {

    public static class Revision {
        private org.apache.commons.jrcs.diff.Revision revision;
        private String text1;
        private String text2;

        public Revision(String text1, String text2) {
            this.text1 = text1;
            this.text2 = text2;

            try {
                this.revision = Diff.diff(ToString.stringToArray(text1), ToString.stringToArray(text2));
            } catch (DifferentiationFailedException e) {
                this.revision = null;
                e.printStackTrace();
            }
        }

        public org.apache.commons.jrcs.diff.Revision getRevision() {
            return revision;
        }

        public void setRevision(org.apache.commons.jrcs.diff.Revision revision) {
            this.revision = revision;
        }

        public String getText1() {
            return text1;
        }

        public void setText1(String text1) {
            this.text1 = text1;
        }

        public String getText2() {
            return text2;
        }

        public void setText2(String text2) {
            this.text2 = text2;
        }

    }

    public static Revision diff(String text1, String text2) {
        return new Revision(text1, text2);
    }
}
