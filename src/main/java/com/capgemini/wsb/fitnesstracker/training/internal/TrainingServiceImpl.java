package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingNotFoundException;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.internal.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingProvider {

    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<Training> getTraining(Long trainingId) {
        return trainingRepository.findById(trainingId);
    }

    public Training createTraining(Training training) {
        return trainingRepository.save(training);
    }

    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    public List<Training> getTrainingsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return trainingRepository.findAll()
                .stream()
                .filter(training -> training.getUser().equals(user))
                .toList();
    }

    public List<Training> getTrainingsByActivityType(ActivityType activityType) {
        return trainingRepository.findAll()
                .stream()
                .filter(training -> training.getActivityType() == activityType)
                .toList();
    }

    public List<Training> getCompletedTrainingsAfterDate(LocalDate endDate) {
        return trainingRepository.findAll()
                .stream()
                .filter(training -> {
                    Date trainingEndDate = training.getEndTime();
                    LocalDate trainingEndLocalDate = Instant.ofEpochMilli(trainingEndDate.getTime())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    return trainingEndLocalDate.isAfter(endDate);
                })
                .toList();
    }

    public Training updateTraining(Long id, Training updatedTraining) {
        Training existingTraining = trainingRepository.findById(id)
                .orElseThrow(() -> new TrainingNotFoundException(id));

        existingTraining.setStartTime(updatedTraining.getStartTime());
        existingTraining.setEndTime(updatedTraining.getEndTime());
        existingTraining.setActivityType(updatedTraining.getActivityType());
        existingTraining.setDistance(updatedTraining.getDistance());
        existingTraining.setAverageSpeed(updatedTraining.getAverageSpeed());

        return trainingRepository.save(existingTraining);
    }

    public void deleteTraining(Long id) {
        trainingRepository.deleteById(id);
    }
}
