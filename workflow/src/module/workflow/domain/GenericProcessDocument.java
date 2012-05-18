package module.workflow.domain;

import java.util.HashMap;
import java.util.Map;

import module.workflow.util.WorkflowDocumentUploadBean;

/**
 * 
 * @author João Antunes (joao.antunes@tagus.ist.utl.pt) - 7 de Mai de 2012
 * 
 *         Generic document associated with a process, it supports a simple
 *         description
 * 
 */
public class GenericProcessDocument extends GenericProcessDocument_Base {
    
    public  GenericProcessDocument() {
        super();
    }
    
    public class GenericPDMetaDataResolver extends ProcessDocumentMetaDataResolver<GenericProcessDocument> {

	private static final String TEMPLATE = "Outros";

	@Override
	public String getMetadataTemplateNameToUseOrCreate() {
	    return TEMPLATE;
	}

	@Override
	public Map<String, String> getMetadataKeysAndValuesMap(GenericProcessDocument processDocument) {
	    HashMap<String, String> hashMap = new HashMap<String, String>();
	    hashMap.put("Nome atríbuido ao ficheiro", processDocument.getGenericDescription());
	    return hashMap;
	}

    }

    public GenericProcessDocument(String displayName, String filename, byte[] content, WorkflowProcess process) {
	super();
	init(displayName, filename, content, process);

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
