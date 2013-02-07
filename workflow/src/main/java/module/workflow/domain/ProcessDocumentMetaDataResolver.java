/**
 * 
 */
package module.workflow.domain;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import module.fileManagement.domain.Document;
import module.fileManagement.domain.metadata.Metadata;
import module.fileManagement.domain.metadata.MetadataTemplate;
import pt.ist.bennu.core.domain.groups.PersistentGroup;
import pt.ist.bennu.core.util.BundleUtil;

/**
 * @author João Antunes (joao.antunes@tagus.ist.utl.pt) - 4 de Mai de 2012
 * 
 *         Class used to transform domain data into fileManagement metadata and
 *         to use the Metadata on already existing documents, into domain data.
 * 
 */
public abstract class ProcessDocumentMetaDataResolver<P extends ProcessFile> {

    // Start of common metadata keys
    /**
     * Common {@link Metadata} key whose value will be the identification number
     * of the process
     */
    public final static String PROCESS_IDENTIFIER = "Id. do processo";
    /**
     * It has the name - in case it exists - of the type of process
     */
    public final static String PROCESS_TYPE = "Tipo de processo";

    /**
     * 
     * @return A string that represents the name of the MetadataTemplate to be
     *         retrieved or to be created if it doesn't exist
     */
    public String getMetadataTemplateNameToUseOrCreate(Class<? extends ProcessFile> classToUse) {
        return BundleUtil.getLocalizedNamedFroClass(classToUse);
    }

    public Map<String, String> getMetadataKeysAndValuesMap(P processDocument) {
        Map<String, String> metadataMap = new HashMap<String, String>();
        metadataMap.put(PROCESS_IDENTIFIER, processDocument.getProcess().getProcessNumber());
        metadataMap.put(PROCESS_TYPE, BundleUtil.getLocalizedNamedFroClass(processDocument.getProcess().getClass()));
        return metadataMap;
    }

    /**
     * 
     * @return The {@link PersistentGroup#getClass()} to be used on the Document
     *         to control the read capabilities {@link Document#getReadGroup()} the default is {@link WFDocsDefaultReadGroup}
     */
    public @Nonnull
    Class<? extends AbstractWFDocsGroup> getReadGroupClass() {
        return WFDocsDefaultReadGroup.class;
    }

    /**
     * 
     * @return The {@link PersistentGroup#getClass()} to be used on the Document
     *         to control the write capabilities {@link Document#getWriteGroup()}
     */
    public abstract @Nonnull
    Class<? extends AbstractWFDocsGroup> getWriteGroupClass();

    /**
     * 
     * @return if true, the new instance of P (which extends {@link ProcessDocument}) will have its access registered, if
     *         false it will not
     */
    public boolean shouldFileContentAccessBeLogged() {
        return false;
    }

    @SuppressWarnings("unchecked")
    public void fillMetaDataBasedOnDocument(ProcessFile file) {
        MetadataTemplate metadataTemplate =
                MetadataTemplate.getOrCreateInstance(getMetadataTemplateNameToUseOrCreate(file.getClass()));

        Map<String, String> metadataKeysAndValuesMap = getMetadataKeysAndValuesMap((P) file);

        metadataTemplate.addKeysStrings(metadataKeysAndValuesMap.keySet());

        file.getDocument().setMetadataTemplateAssociated(metadataTemplate);
        //TODO change the method to addOrAppendMetadata - so that nothing is lost
        file.getDocument().addMetadata(metadataKeysAndValuesMap);
        file.getDocument().setSaveAccessLog(shouldFileContentAccessBeLogged());

    }

}
