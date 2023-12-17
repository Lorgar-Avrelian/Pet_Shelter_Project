package sky.pro.Animals.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sky.pro.Animals.entity.DailyReport;
import sky.pro.Animals.repository.DailyReportRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static sky.pro.Animals.constants.Constants.*;

@ExtendWith(MockitoExtension.class)
class DailyReportServiceImplTest {
    @Mock
    ClientServiceImpl clientService;
    @Mock
    DailyReportRepository dailyReportRepository;
    @InjectMocks
    DailyReportServiceImpl dailyReportService;

    @BeforeEach
    void init() {
        lenient().when(dailyReportRepository.findAll()).thenReturn(DAILY_REPORTS);
        lenient().when(clientService.getById(anyLong())).thenReturn(CLIENT_5);
        lenient().when(dailyReportRepository.findById(anyLong())).thenReturn(Optional.of(REPORT_1));
        lenient().when(dailyReportRepository.save(any(DailyReport.class))).thenReturn(REPORT_1);
        lenient().doNothing().when(dailyReportRepository).deleteById(anyLong());
    }

    @Test
    void getAll() {
        assertIterableEquals(REPORT_LIST, dailyReportService.getAll());
    }

    @Test
    void getAllClientReports() {
        assertEquals(DAILY_REPORTS_MAP, dailyReportService.getAllClientReports(CLIENT_5.getId()));
    }

    @Test
    void getReport() {
        assertEquals(REPORT_1, dailyReportService.getReport(REPORT_1.getId()));
    }

    @Test
    void saveReport() {
        dailyReportService.saveReport(REPORT_1);
        verify(dailyReportRepository, times(1)).save(REPORT_1);
    }

    @Test
    void deleteReports() {
        dailyReportService.deleteReports(CLIENT_5.getId());
        verify(clientService, times(1)).getById(CLIENT_5.getId());
        verify(dailyReportRepository, times(1)).findAll();
    }
}