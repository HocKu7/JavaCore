package main.storage.initor;

import main.storage.initor.fileInitor.InTextFileStorageInitor;
import main.storage.initor.fileInitor.XmlDomFileDataInitor;
import main.storage.initor.fileInitor.XmlDomFileInitorMultiThread;
import main.storage.initor.fileInitor.XmlSAXFileInitor;

public class StorageInitorFactory {
    private StorageInitorFactory(){

    }

    public static StorageInitor getStorageInitor(InitStorageType initStorageType){
        switch (initStorageType){

            case MEMORY: {
                return new InMemoryStorageInitor();
            }
            case TEXT_FILE: {
                return new InTextFileStorageInitor();
            }
            case XML_FILE:{
                return new XmlDomFileDataInitor();
            }
            case SAX_PARSE:{
                return new XmlSAXFileInitor();
            }
            case XML_MULTITHREAD:{
                return new XmlDomFileInitorMultiThread();
            }
            default:{
                throw new RuntimeException("Unknown main.storage init type " + initStorageType);
            }
        }
    }

}
