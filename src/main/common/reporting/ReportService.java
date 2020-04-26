package main.common.reporting;

import main.common.business.exception.checked.ReportException;

public interface ReportService {

    void exportData() throws ReportException;
}
