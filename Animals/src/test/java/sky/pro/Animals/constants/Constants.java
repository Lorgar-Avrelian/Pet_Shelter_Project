package sky.pro.Animals.constants;

import liquibase.repackaged.org.apache.commons.collections4.ListUtils;
import sky.pro.Animals.entity.*;
import sky.pro.Animals.model.PetVariety;

import java.sql.Date;
import java.util.*;

public class Constants {
    public static final Client CLIENT_1 = new Client(1L, "Name 1", "Surname 1", "Username 1", "Address 1", Date.valueOf("2000-01-01"), "Passport 1", 111111L, null);
    public static final Client CLIENT_2 = new Client(2L, "Name 2", "Surname 2", "Username 2", "Address 2", Date.valueOf("2000-01-02"), "Passport 2", 222222L, null);
    public static final Client CLIENT_3 = new Client(3L, "Name 3", "Surname 3", "Username 3", "Address 3", Date.valueOf("2000-01-03"), "Passport 3", 333333L, null);
    public static final Client CLIENT_4 = new Client(4L, "Name 4", "Surname 4", "Username 4", "Address 4", Date.valueOf("2000-01-04"), "Passport 4", 444444L, null);
    public static final Client CLIENT_5 = new Client(5L, "Name 5", "Surname 5", "Username 5", "Address 5", Date.valueOf("2000-01-05"), "Passport 5", 555555L, null);
    public static final List<Client> CLIENTS = new ArrayList<>(List.of(CLIENT_1, CLIENT_2, CLIENT_3, CLIENT_4, CLIENT_5));
    public static final Pet CAT_1 = new Pet(1L, "CAT 1", Date.valueOf("2020-01-01"), true, PetVariety.cat, null);
    public static final Pet CAT_2 = new Pet(2L, "CAT 2", Date.valueOf("2020-01-02"), true, PetVariety.cat, null);
    public static final Pet CAT_3 = new Pet(3L, "CAT 3", Date.valueOf("2020-01-03"), true, PetVariety.cat, CLIENT_5);
    public static final Pet CAT_4 = new Pet(4L, "CAT 4", Date.valueOf("2020-01-04"), true, PetVariety.cat, CLIENT_5);
    public static final Pet CAT_5 = new Pet(5L, "CAT 5", Date.valueOf("2020-01-05"), true, PetVariety.cat, CLIENT_5);
    public static final List<Pet> CATS = new ArrayList<>(List.of(CAT_1, CAT_2, CAT_3, CAT_4, CAT_5));
    public static final Pet DOG_1 = new Pet(6L, "DOG 1", Date.valueOf("2021-01-01"), true, PetVariety.dog, null);
    public static final Pet DOG_2 = new Pet(7L, "DOG 2", Date.valueOf("2021-01-02"), true, PetVariety.dog, null);
    public static final Pet DOG_3 = new Pet(8L, "DOG 3", Date.valueOf("2021-01-03"), true, PetVariety.dog, null);
    public static final Pet DOG_4 = new Pet(9L, "DOG 4", Date.valueOf("2021-01-04"), true, PetVariety.dog, null);
    public static final Pet DOG_5 = new Pet(10L, "DOG 5", Date.valueOf("2021-01-05"), true, PetVariety.dog, null);
    public static final List<Pet> DOGS = new ArrayList<>(List.of(DOG_1, DOG_2, DOG_3, DOG_4, DOG_5));
    public static final List<Pet> PETS = ListUtils.union(CATS, DOGS);
    public static final Volunteer VOLUNTEER_1 = new Volunteer(1L, "Name 1", "Address 1", Date.valueOf("2001-01-01"), "Passport 1", 111L, "employee");
    public static final Volunteer VOLUNTEER_2 = new Volunteer(2L, "Name 2", "Address 2", Date.valueOf("2001-01-02"), "Passport 2", 222L, "employee");
    public static final Volunteer VOLUNTEER_3 = new Volunteer(3L, "Name 3", "Address 3", Date.valueOf("2001-01-03"), "Passport 3", 333L, "employee");
    public static final Volunteer VOLUNTEER_4 = new Volunteer(4L, "Name 4", "Address 4", Date.valueOf("2001-01-04"), "Passport 4", 444L, "employee");
    public static final Volunteer VOLUNTEER_5 = new Volunteer(5L, "Name 5", "Address 5", Date.valueOf("2001-01-05"), "Passport 5", 555L, "admin");
    public static final List<Volunteer> VOLUNTEERS = new ArrayList<>(List.of(VOLUNTEER_1, VOLUNTEER_2, VOLUNTEER_3, VOLUNTEER_4, VOLUNTEER_5));
    public static final DailyReport REPORT_1 = new DailyReport(1L, HexFormat.of().parseHex("1111"), "Text 1", Date.valueOf("2023-01-01"), 5L, false);
    public static final DailyReport REPORT_2 = new DailyReport(2L, HexFormat.of().parseHex("2222"), "Text 2", Date.valueOf("2023-01-02"), 5L, false);
    public static final DailyReport REPORT_3 = new DailyReport(3L, HexFormat.of().parseHex("3333"), "Text 3", Date.valueOf("2023-01-03"), 5L, true);
    public static final List<DailyReport> DAILY_REPORTS = new ArrayList<>(List.of(REPORT_1, REPORT_2, REPORT_3));
    public static final HashMap<Long, Date> DAILY_REPORTS_MAP = new HashMap<>(Map.of(1L, Date.valueOf("2023-01-01"), 2L, Date.valueOf("2023-01-02"), 3L, Date.valueOf("2023-01-03")));
    public static final DailyReportList REPORT_LIST_1 = new DailyReportList(1L, Date.valueOf("2023-01-01"), 5L);
    public static final DailyReportList REPORT_LIST_2 = new DailyReportList(2L, Date.valueOf("2023-01-02"), 5L);
    public static final DailyReportList REPORT_LIST_3 = new DailyReportList(3L, Date.valueOf("2023-01-03"), 5L);
    public static final List<DailyReportList> REPORT_LIST = new ArrayList<>(List.of(REPORT_LIST_1, REPORT_LIST_2, REPORT_LIST_3));
    public static final ProbationPeriod PERIOD_1 = new ProbationPeriod(1L, 5L, 5L, Date.valueOf("2024-01-01"));
    public static final ProbationPeriod PERIOD_2 = new ProbationPeriod(2L, 5L, 4L, Date.valueOf("2024-01-02"));
    public static final ProbationPeriod PERIOD_3 = new ProbationPeriod(3L, 5L, 3L, Date.valueOf("2024-01-03"));
    public static final List<ProbationPeriod> PERIODS = new ArrayList<>(List.of(PERIOD_1, PERIOD_2, PERIOD_3));
    public static final Info INFO_1 = new Info(1L, "Key 1", "Text 1");
    public static final Info INFO_2 = new Info(2L, "Key 2", "Text 2");
    public static final Info INFO_3 = new Info(3L, "Key 3", "Text 3");
    public static final Info INFO_4 = new Info(4L, "Key 4", "Text 4");
    public static final Info INFO_5 = new Info(5L, "Key 5", "Text 5");
    public static final List<Info> INFO_LIST = new ArrayList<>(List.of(INFO_1, INFO_2, INFO_3, INFO_4, INFO_5));
}
