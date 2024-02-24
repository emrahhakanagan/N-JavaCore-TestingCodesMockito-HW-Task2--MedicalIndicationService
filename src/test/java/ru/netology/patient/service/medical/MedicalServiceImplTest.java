package ru.netology.patient.service.medical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalServiceImplTest {
    @Mock
    PatientInfoRepository patientInfoRepository;
    @Mock
    SendAlertService sendAlertService;
    @InjectMocks
    MedicalServiceImpl medicalService;

    private PatientInfo patient;

    @BeforeEach
    void setup() {
        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);

        patient = new PatientInfo(
                "Иван", "Петров", LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))
        );
    }

    @Test
    void checkTemperature_Should_SendAlert() {
//        var patient = new PatientInfo(
//                "Иван", "Петров", LocalDate.of(1980, 11, 26),
//                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))
//        );  // Norm  Temp 36.65

        when(patientInfoRepository.getById(any()))
                .thenReturn(patient);

        BigDecimal currentTemp = BigDecimal.valueOf(12);
        medicalService.checkTemperature(patient.getId(), currentTemp);

        verify(sendAlertService, Mockito.times(1)).send(anyString());
    }

    @Test
    void checkTemperature_Should_SendAlert_WithArgumentCapture() {
//        var patient = new PatientInfo(
//                "Иван", "Петров", LocalDate.of(1980, 11, 26),
//                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))
//        );  // Norm  Temp 36.65

        when(patientInfoRepository.getById(any()))
                .thenReturn(patient);

        BigDecimal currentTemp = BigDecimal.valueOf(12);
        medicalService.checkTemperature(patient.getId(), currentTemp);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        String messageExpected = String.format("Warning, patient with id: %s, need help", patient.getId());

        verify(sendAlertService).send(argumentCaptor.capture());

        Assertions.assertEquals(messageExpected, argumentCaptor.getValue());
    }

    @Test
    void checkTemperature_ShouldNot_SendAlert() {
//        var patient = new PatientInfo(
//                "Иван", "Петров", LocalDate.of(1980, 11, 26),
//                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))
//        );  // Norm  Temp 36.65

        when(patientInfoRepository.getById(any()))
                .thenReturn(patient);

        BigDecimal currentTemp = BigDecimal.valueOf(37);
        medicalService.checkTemperature(patient.getId(), currentTemp);

        verify(sendAlertService, Mockito.never()).send(anyString());
    }


    @Test
    void checkBloodPressure_Should_SendAlert() {
//        var patient = new PatientInfo(
//                "Иван", "Петров", LocalDate.of(1980, 11, 26),
//                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))
//        );  // Norm  BloodPres 120 80

        when(patientInfoRepository.getById(any()))
                .thenReturn(patient);

        BloodPressure currentPressure = new BloodPressure(60, 120);
        medicalService.checkBloodPressure(patient.getId(), currentPressure);

        verify(sendAlertService, Mockito.times(1)).send(anyString());
    }

    @Test
    void checkBloodPressure_Should_SendAlert_WithArgumentCapture() {
//        var patient = new PatientInfo(
//                "Иван", "Петров", LocalDate.of(1980, 11, 26),
//                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))
//        );  // Norm  Temp 36.65

        when(patientInfoRepository.getById(any()))
                .thenReturn(patient);

        BloodPressure currentPressure = new BloodPressure(60, 120);
        medicalService.checkBloodPressure(patient.getId(), currentPressure);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        String messageExpected = String.format("Warning, patient with id: %s, need help", patient.getId());

        verify(sendAlertService).send(argumentCaptor.capture());

        Assertions.assertEquals(messageExpected, argumentCaptor.getValue());
    }


    @Test
    void checkBloodPressure_ShouldNot_SendAlert() {
//        var patient = new PatientInfo(
//                "Иван", "Петров", LocalDate.of(1980, 11, 26),
//                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))
//        );

        when(patientInfoRepository.getById(any()))
                .thenReturn(patient);

        BloodPressure currentPressure = new BloodPressure(120, 80);
        medicalService.checkBloodPressure(patient.getId(), currentPressure);

        verify(sendAlertService, Mockito.never()).send(anyString());
    }

}