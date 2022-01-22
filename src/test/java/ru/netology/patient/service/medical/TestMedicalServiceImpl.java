package ru.netology.patient.service.medical;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestMedicalServiceImpl {
    PatientInfoRepository patientInfoRepository;

    @Test
    public void testCheckBloodPressure() {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.any()))
                .thenReturn(new PatientInfo("2", "Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                        new HealthInfo(new BigDecimal("36.6"), new BloodPressure(140, 90))));

        SendAlertService sendAlertService = Mockito.mock(SendAlertServiceImpl.class);

        BloodPressure currentPressure = new BloodPressure(120, 80);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure("2", currentPressure);

        Mockito.verify(sendAlertService, Mockito.times(1)).send(Mockito.any());
    }

    @Test
    public void testCheckTemperature() {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.any()))
                .thenReturn(new PatientInfo("3", "Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                        new HealthInfo(new BigDecimal("38.5"), new BloodPressure(120, 80))));

        SendAlertService sendAlertService = Mockito.mock(SendAlertServiceImpl.class);

        BigDecimal currentTemperature = new BigDecimal("36.6");
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkTemperature("3", currentTemperature);

        Mockito.verify(sendAlertService, Mockito.times(1)).send(Mockito.any());
    }

    @Test
    public void testNotMessageWhenNorm() {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.any()))
                .thenReturn(new PatientInfo("4", "Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                        new HealthInfo(new BigDecimal("36.5"), new BloodPressure(120, 80))));

        SendAlertService sendAlertService = Mockito.mock(SendAlertServiceImpl.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        BloodPressure currentPressure = new BloodPressure(120, 80);
        BigDecimal currentTemperature = new BigDecimal("36.6");
        medicalService.checkBloodPressure("4", currentPressure);
        medicalService.checkTemperature("4", currentTemperature);

        Mockito.verify(sendAlertService, Mockito.never()).send(Mockito.any());
    }
}
