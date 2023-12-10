package sky.pro.Animals.service;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import sky.pro.Animals.entity.DailyReport;
import sky.pro.Animals.entity.DailyReportList;
import sky.pro.Animals.repository.DailyReportRepository;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for working with daily reports data
 * <p>
 * <hr>
 * <p>
 * Сервис для работы с данными ежедневных отчётов
 */
@Service
@Log4j
public class DailyReportServiceImpl implements DailyReportService {
    private final ClientServiceImpl clientService;
    private final DailyReportRepository dailyReportRepository;

    public DailyReportServiceImpl(ClientServiceImpl clientService, DailyReportRepository dailyReportRepository) {
        this.clientService = clientService;
        this.dailyReportRepository = dailyReportRepository;
    }

    /**
     * Method for getting collection with list of all daily reports. <br>
     * Used repository method {@link DailyReportRepository#findAll()}. <br>
     * <hr>
     * Метод для получения коллекции со списком всех ежедневных отчётов. <br>
     * Использован метод репозитория {@link DailyReportRepository#findAll()}. <br>
     * <hr>
     *
     * @return Collection with list of all daily reports / Коллекция со списком всех ежедневных отчётов
     * @see DailyReportRepository#findAll()
     */
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

    /**
     * Method for getting all daily reports of the client with this id. <br>
     * Used services methods {@link DailyReportService#getAll()} and {@link ClientService#getById(Long)}. <br>
     * <hr>
     * Метод для получения всех ежедневных отчётов клиента с данным id. <br>
     * Использованы методы сервисов {@link DailyReportService#getAll()} и {@link ClientService#getById(Long)}. <br>
     * <hr>
     *
     * @param clientId
     * @return Collection with all daily reports of the client / Коллекцию со всеми ежедневными отчётами клиента
     * @see DailyReportService#getAll()
     * @see ClientService#getById(Long)
     */
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

    /**
     * Method for getting daily report with this id. <br>
     * Used repository method {@link DailyReportRepository#findById(Object)}. <br>
     * <hr>
     * Метод для получения ежедневного отчёта с данным id. <br>
     * Использован метод репозитория {@link DailyReportRepository#findById(Object)}. <br>
     * <hr>
     *
     * @param reportId
     * @return Daily report / Ежедневный отчёт
     * @see DailyReportRepository#findById(Object)
     */
    @Override
    public DailyReport getReport(Long reportId) {
        try {
            return dailyReportRepository.findById(reportId).get();
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * Method for saving daily report in DB. <br>
     * Used repository method {@link DailyReportRepository#save(Object)}. <br>
     * <hr>
     * Метод для сохранения ежедневного отчёта в БД. <br>
     * Использован метод репозитория {@link DailyReportRepository#save(Object)}. <br>
     * <hr>
     *
     * @param dailyReport
     * @see DailyReportRepository#save(Object)
     */
    @Override
    public void saveReport(DailyReport dailyReport) {
        dailyReportRepository.save(dailyReport);
    }

    /**
     * Method for deleting daily report from DB. <br>
     * Used repository method {@link DailyReportRepository#deleteById(Object)}. <br>
     * <hr>
     * Метод для удаления ежедневного отчёта из БД. <br>
     * Использован метод репозитория {@link DailyReportRepository#deleteById(Object)}. <br>
     * <hr>
     *
     * @param clientId
     * @see DailyReportRepository#deleteById(Object)
     */
    @Override
    public void deleteReports(Long clientId) {
        Set<Long> clientReports = getAllClientReports(clientId).keySet();
        for (Long report : clientReports) {
            dailyReportRepository.deleteById(report);
        }
    }
}
