package sky.pro.Animals.service;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import sky.pro.Animals.entity.DailyReport;
import sky.pro.Animals.entity.DailyReportList;
import sky.pro.Animals.repository.DailyReportRepository;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j
public class DailyReportServiceImpl implements DailyReportService {
    private final ClientServiceImpl clientService;
    private final DailyReportRepository dailyReportRepository;

    public DailyReportServiceImpl(ClientServiceImpl clientService, DailyReportRepository dailyReportRepository) {
        this.clientService = clientService;
        this.dailyReportRepository = dailyReportRepository;
    }

    @Override
    public Collection<DailyReportList> getAll() {
        Collection<DailyReport> dailyReports = dailyReportRepository.findAll();
        Collection<DailyReportList> dailyReportList = new LinkedList<>();
        for (DailyReport dailyReport : dailyReports) {
            DailyReportList newDailyReport = new DailyReportList(dailyReport.getId(), dailyReport.getDate(), dailyReport.getClientId());
            dailyReportList.add(newDailyReport);
        }
        return dailyReportList;
    }

    @Override
    public HashMap<Long, Date> getAllClientReports(Long clientId) {
        if (clientService.getById(clientId) == null) {
            return null;
        }
        Collection<DailyReportList> dailyReports = getAll().stream()
                .filter(dailyReportList -> dailyReportList.getClientId().equals(clientId))
                .collect(Collectors.toCollection(LinkedList::new));
        HashMap<Long, Date> dailyReportMap = new HashMap<>();
        for (DailyReportList dailyReport : dailyReports) {
            dailyReportMap.put(dailyReport.getId(), dailyReport.getDate());
        }
        return dailyReportMap;
    }

    @Override
    public DailyReport getReport(Long reportId) {
        try {
            return dailyReportRepository.findById(reportId).get();
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void saveReport(DailyReport dailyReport) {
        dailyReportRepository.save(dailyReport);
    }

    @Override
    public void deleteReports(Long clientId) {
        Set<Long> clientReports = getAllClientReports(clientId).keySet();
        for (Long report : clientReports) {
            dailyReportRepository.deleteById(report);
        }
    }
}
