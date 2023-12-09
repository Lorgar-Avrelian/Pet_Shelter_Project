package sky.pro.Animals.service;

import sky.pro.Animals.entity.DailyReport;
import sky.pro.Animals.entity.DailyReportList;

import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;

public interface DailyReportService {
    Collection<DailyReportList> getAll();

    HashMap<Long, Date> getAllClientReports(Long clientId);

    DailyReport getReport(Long reportId);

    void saveReport(DailyReport dailyReport);

    void deleteReports(Long clientId);
}
