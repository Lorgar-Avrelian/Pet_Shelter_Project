package sky.pro.Animals.service;

import sky.pro.Animals.entity.Client;

public interface ProbationPeriodService {
    Client changeLastDay(Client client, int days);
}
