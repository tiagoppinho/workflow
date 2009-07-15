package module.metaWorkflow.util.versioning;

import java.io.Serializable;

import org.apache.commons.jrcs.diff.Diff;
import org.apache.commons.jrcs.diff.DifferentiationFailedException;
import org.apache.commons.jrcs.util.ToString;

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
