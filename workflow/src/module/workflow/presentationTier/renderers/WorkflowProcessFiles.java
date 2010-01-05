package module.workflow.presentationTier.renderers;

import java.util.Iterator;
import java.util.List;

import module.workflow.domain.ProcessFile;
import module.workflow.domain.WorkflowProcess;
import myorg.util.BundleUtil;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlParagraphContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class WorkflowProcessFiles extends OutputRenderer {

    private String downloadFormat;
    private String removeFormat;

    public String getDownloadFormat() {
	return downloadFormat;
    }

    public void setDownloadFormat(String downloadFormat) {
	this.downloadFormat = downloadFormat;
    }

    public String getRemoveFormat() {
	return removeFormat;
    }

    public void setRemoveFormat(String removeFormat) {
	this.removeFormat = removeFormat;
    }

    @Override
    protected Layout getLayout(Object arg0, Class arg1) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object arg0, Class arg1) {
		WorkflowProcess process = (WorkflowProcess) arg0;

		HtmlBlockContainer container = new HtmlBlockContainer();

		for (Class<? extends ProcessFile> fileType : process.getAvailableFileTypes()) {
		    container.addChild(generate(process, fileType));
		}

		return container;
	    }

	    private HtmlComponent generate(WorkflowProcess process, Class<? extends ProcessFile> fileType) {
		List<? extends ProcessFile> files = process.getFiles(fileType);

		HtmlBlockContainer blockContainer = new HtmlBlockContainer();
		HtmlParagraphContainer container = new HtmlParagraphContainer();
		blockContainer.addChild(container);

		container.addChild(new HtmlText(BundleUtil.getLocalizedNamedFroClass(fileType) + ": "));
		if (files.isEmpty()) {
		    container.addChild(new HtmlText("-"));
		} else {
		    Iterator<? extends ProcessFile> iterator = files.iterator();

		    while (iterator.hasNext()) {
			ProcessFile file = iterator.next();

			HtmlLink downloadLink = new HtmlLink();
			downloadLink.setBody(new HtmlText(file.getDisplayName()));
			downloadLink.setUrl(RenderUtils.getFormattedProperties(getDownloadFormat(), file));
			container.addChild(downloadLink);

			if (file.isPossibleToArchieve()) {
			    HtmlLink removeLink = new HtmlLink();
			    removeLink.setBody(new HtmlText("("
				    + RenderUtils.getResourceString("WORKFLOW_RESOURCES", "link.removeFile") + ")"));
			    removeLink.setUrl(RenderUtils.getFormattedProperties(getRemoveFormat(), file));

			    container.addChild(removeLink);
			}

			if (iterator.hasNext()) {
			    container.addChild(new HtmlText(", "));
			}
		    }
		}
		return blockContainer;

	    }

	};
    }
}
