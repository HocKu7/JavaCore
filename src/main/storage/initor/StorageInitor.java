package main.storage.initor;

import main.common.business.exception.checked.InitStorageException;

public interface StorageInitor {
  void initStorage() throws InitStorageException;
}
