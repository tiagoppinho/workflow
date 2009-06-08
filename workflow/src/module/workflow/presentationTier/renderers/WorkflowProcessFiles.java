package module.workflow.presentationTier.renderers;

import java.util.Iterator;
import java.util.List;

import module.workflow.domain.GenericFile;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.FileTypeNameResolver;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
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

		for (Class<? extends GenericFile> fileType : process.getAvailableFileTypes()) {
		    container.addChild(generate(process, fileType));
		}

		return container;
	    }

	    private HtmlComponent generate(WorkflowProcess process, Class<? extends GenericFile> fileType) {
		List<? extends GenericFile> files = process.getFiles(fileType);
		HtmlInlineContainer container = new HtmlInlineContainer();

		container.addChild(new HtmlText(FileTypeNameResolver.getNameFor(fileType) + ": "));
		if (files.isEmpty()) {
		    container.addChild(new HtmlText("-"));
		} else {
		    Iterator<? extends GenericFile> iterator = files.iterator();

		    while (iterator.hasNext()) {
			GenericFile file = iterator.next();

			HtmlLink downloadLink = new HtmlLink();
			downloadLink.setBody(new HtmlText(file.getDisplayName()));
			downloadLink.setUrl(RenderUtils.getFormattedProperties(getDownloadFormat(), file));
			container.addChild(downloadLink);

			HtmlLink removeLink = new HtmlLink();
			removeLink.setBody(new HtmlText("("
				+ RenderUtils.getResourceString("WORKFLOW_RESOURCES", "link.removeFile") + ")"));
			removeLink.setUrl(RenderUtils.getFormattedProperties(getRemoveFormat(), file));

			container.addChild(removeLink);

			if (iterator.hasNext()) {
			    container.addChild(new HtmlText(", "));
			}
		    }
		}
		return container;

	    }

	};
    }
}
