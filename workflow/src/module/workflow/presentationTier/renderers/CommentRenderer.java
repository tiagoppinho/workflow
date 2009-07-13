package module.workflow.presentationTier.renderers;

import java.util.Set;
import java.util.TreeSet;

import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowProcessComment;
import myorg.domain.User;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.rendererExtensions.DateTimeAsDateRenderer;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlImage;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlParagraphContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class CommentRenderer extends OutputRenderer {

    private String commentBlockClasses;
    private String nameClasses;
    private String photoClasses;
    private String bodyClasses;
    private String photoUrl;
    private boolean reverseOrder;

    public boolean isReverseOrder() {
	return reverseOrder;
    }

    public void setReverseOrder(boolean reverseOrder) {
	this.reverseOrder = reverseOrder;
    }

    public String getCommentBlockClasses() {
	return commentBlockClasses;
    }

    public void setCommentBlockClasses(String commentBlockClasses) {
	this.commentBlockClasses = commentBlockClasses;
    }

    public String getPhotoClasses() {
	return photoClasses;
    }

    public void setPhotoClasses(String photoClasses) {
	this.photoClasses = photoClasses;
    }

    public String getBodyClasses() {
	return bodyClasses;
    }

    public void setBodyClasses(String bodyClasses) {
	this.bodyClasses = bodyClasses;
    }

    public String getNameClasses() {
	return nameClasses;
    }

    public void setNameClasses(String nameClasses) {
	this.nameClasses = nameClasses;
    }

    public String getPhotoUrl() {
	return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
	this.photoUrl = photoUrl;
    }

    @Override
    protected Layout getLayout(Object arg0, Class arg1) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object arg0, Class arg1) {
		HtmlBlockContainer outterContainer = new HtmlBlockContainer();
		WorkflowProcess process = (WorkflowProcess) arg0;
		Set<WorkflowProcessComment> comments = new TreeSet<WorkflowProcessComment>(
			isReverseOrder() ? WorkflowProcessComment.REVERSE_COMPARATOR : WorkflowProcessComment.COMPARATOR);
		comments.addAll(process.getComments());

		User currentRenderingUser = null;
		HtmlBlockContainer commentContainer = null;

		for (WorkflowProcessComment comment : comments) {
		    if (currentRenderingUser == comment.getCommenter()) {
			commentContainer.addChild(generateSingleComment(comment));
		    } else {
			currentRenderingUser = comment.getCommenter();
			commentContainer = generateNewCommentBlock(outterContainer, comment);
		    }
		}

		return outterContainer;
	    }

	    private HtmlBlockContainer generateNewCommentBlock(HtmlBlockContainer outterContainer, WorkflowProcessComment comment) {
		HtmlBlockContainer commentContainer = new HtmlBlockContainer();
		outterContainer.addChild(commentContainer);
		commentContainer.addClass(getCommentBlockClasses());

		HtmlTable table = new HtmlTable();
		commentContainer.addChild(table);
		HtmlTableRow row = table.createRow();
		HtmlTableCell photoCell = row.createCell();

		photoCell.setClasses(getPhotoClasses());

		HtmlInlineContainer container = new HtmlInlineContainer();
		HtmlImage userPhoto = new HtmlImage();
		container.addChild(userPhoto);
		userPhoto.setSource(RenderUtils.getFormattedProperties(getPhotoUrl(), comment.getCommenter()));
		photoCell.setBody(userPhoto);

		HtmlTableCell bodyCell = row.createCell();
		HtmlBlockContainer bodyBlock = new HtmlBlockContainer();
		bodyCell.setBody(bodyBlock);

		HtmlText name = new HtmlText(comment.getCommenter().getPresentationName());
		name.setClasses(getNameClasses());
		bodyBlock.addChild(name);

		HtmlBlockContainer commentBlock = new HtmlBlockContainer();
		bodyBlock.addChild(commentBlock);

		commentBlock.addChild(generateSingleComment(comment));

		return commentBlock;

	    }

	    private HtmlComponent generateSingleComment(WorkflowProcessComment comment) {
		HtmlParagraphContainer container = new HtmlParagraphContainer();
		HtmlComponent renderedDate = RenderKit.getInstance().render(getOutputContext(), comment.getDate());

		HtmlInlineContainer dateContainer = new HtmlInlineContainer();
		dateContainer.addChild(renderedDate);

		container.addChild(dateContainer);
		container.addChild(new HtmlText(": "));
		container.addChild(new HtmlText(comment.getComment()));

		return container;
	    }

	};
    }
}
