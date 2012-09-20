package module.workflow.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pt.ist.bennu.core.util.ClassNameBundle;

import module.fileManagement.domain.FileNode;
import module.workflow.util.WorkflowDocumentUploadBean;

/**
 * 
 * @author João Antunes (joao.antunes@tagus.ist.utl.pt) - 7 de Mai de 2012
 * 
 *         Generic document associated with a process, it supports a simple
 *         description
 * 
 */
@ClassNameBundle(bundle = "resources/WorkflowResources")
public class GenericProcessDocument extends GenericProcessDocument_Base {
    
    public  GenericProcessDocument() {
        super();
    }
    
    public GenericProcessDocument(final FileNode associatedFileNode) {
	super();
	init(associatedFileNode);
    }

    public class GenericPDMetaDataResolver extends ProcessDocumentMetaDataResolver<GenericProcessDocument> {

	private static final String TEMPLATE = "Outros";

	@Override
	public String getMetadataTemplateNameToUseOrCreate() {
	    return TEMPLATE;
	}

	@Override
	public boolean shouldFileContentAccessBeLogged() {
	    return true;
	}

	@Override
	public Map<String, String> getMetadataKeysAndValuesMap(GenericProcessDocument processDocument) {
	    //	    hashMap.put("Nome atríbuido ao ficheiro", processDocument.getGenericDescription());
	    return Collections.emptyMap();
	}

	@Override
	public Class<? extends AbstractWFDocsGroup> getReadGroupClass() {
	    return WFDocsDefaultReadGroup.class;
	}

	@Override
	public Class<? extends AbstractWFDocsGroup> getWriteGroupClass() {
	    return WFDocsDefaultWriteGroup.class;
	}

    }

    @Override
    public ProcessDocumentMetaDataResolver<GenericProcessDocument> getMetaDataResolver() {
	return new GenericPDMetaDataResolver();
    }

    @Override
    public void fillInNonDefaultFields(WorkflowDocumentUploadBean bean) {

    }

    @Override
    public void validateUpload(WorkflowProcess process) {

    }

}
