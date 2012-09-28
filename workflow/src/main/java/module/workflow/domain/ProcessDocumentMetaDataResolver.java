/**
 * 
 */
package module.workflow.domain;

import java.util.Map;

import pt.ist.bennu.core.domain.groups.PersistentGroup;

import module.fileManagement.domain.Document;
import module.fileManagement.domain.metadata.MetadataTemplate;

/**
 * @author João Antunes (joao.antunes@tagus.ist.utl.pt) - 4 de Mai de 2012
 * 
 *         Class used to transform domain data into fileManagement metadata and
 *         to use the Metadata on already existing documents, into domain data.
 * 
 */
public abstract class ProcessDocumentMetaDataResolver<P extends ProcessFile> {

    /**
     * 
     * @return A string that represents the name of the MetadataTemplate to be
     *         retrieved or to be created if it doesn't exist
     */
    public abstract String getMetadataTemplateNameToUseOrCreate();

    public abstract Map<String, String> getMetadataKeysAndValuesMap(P processDocument);

    /**
     * 
     * @return The {@link PersistentGroup#getClass()} to be used on the Document
     *         to control the read capabilities {@link Document#getReadGroup()}
     */
    public abstract Class<? extends AbstractWFDocsGroup> getReadGroupClass();

    /**
     * 
     * @return The {@link PersistentGroup#getClass()} to be used on the Document
     *         to control the write capabilities
     *         {@link Document#getWriteGroup()}
     */
    public abstract Class<? extends AbstractWFDocsGroup> getWriteGroupClass();

    /**
     * 
     * @return if true, the new instance of P (which extends
     *         {@link ProcessDocument}) will have its access registered, if
     *         false it will not
     */
    public boolean shouldFileContentAccessBeLogged() {
	return false;
    }

    @SuppressWarnings("unchecked")
    void fillMetaDataBasedOnDocument(ProcessFile file) {
	MetadataTemplate metadataTemplate = MetadataTemplate.getOrCreateInstance(getMetadataTemplateNameToUseOrCreate());

	Map<String, String> metadataKeysAndValuesMap = getMetadataKeysAndValuesMap((P) file);

	metadataTemplate.addKeysStrings(metadataKeysAndValuesMap.keySet());

	file.getDocument().setMetadataTemplateAssociated(metadataTemplate);
	file.getDocument().addMetadata(metadataKeysAndValuesMap);
	file.getDocument().setSaveAccessLog(shouldFileContentAccessBeLogged());

    }


}
