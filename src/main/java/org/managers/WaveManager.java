package org.managers;

import org.events.Wave;
import org.scenes.Playing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WaveManager {
    private Playing playing;
    private List<Wave> waves;
    private static final int ENEMY_SPAWN_TICK_LIMIT = 60 * 1; //Кол-во секунд между каждым врагом
    private static final int WAVE_TICK_LIMIT = 60 * 5; //Кол-во секунд между волнами
    private int enemySpawnTick;
    private int enemyIndex, waveIndex;
    private int waveTick;
    private boolean waveStartTimer, waveTickTimerOver;

    public WaveManager(Playing playing) {
        this.playing = playing;

        waves = new ArrayList<>();

        enemySpawnTick = ENEMY_SPAWN_TICK_LIMIT;
        waveTick = 0;

        createWaves();
    }

    public void update() {
        if (enemySpawnTick < ENEMY_SPAWN_TICK_LIMIT)
            enemySpawnTick++;

        if (waveStartTimer) {
            waveTick++;
            if (waveTick >= WAVE_TICK_LIMIT) {
                waveTickTimerOver = true;
            }
        }
    }

    public void increaseWaveIndex() { //Переход на следущую волну
        waveIndex++;
        waveTickTimerOver = false;
        waveStartTimer = false;
    }

    public boolean isWaveTimeOver() {
        return waveTickTimerOver;
    }

    public void startWaveTimer() {
        waveStartTimer = true;
    }

    public int getNextEnemy() { //Берёт следующего врага из списка волны
        enemySpawnTick = 0;
        return waves.get(waveIndex).getEnemyList().get(enemyIndex++);
    }

    private void createWaves() {
        waves.add(new Wave(new ArrayList<>(Arrays.asList(0, 3, 0, 2, 1)))); //1
        waves.add(new Wave(new ArrayList<>(Arrays.asList(2, 0, 2, 3, 1, 3, 1)))); //2
        waves.add(new Wave(new ArrayList<>(Arrays.asList(2, 0, 1, 1, 1, 2, 3)))); //3
        waves.add(new Wave(new ArrayList<>(Arrays.asList(2, 2, 3, 0, 1, 3, 0, 1)))); //4
        waves.add(new Wave(new ArrayList<>(Arrays.asList(2, 3, 0, 1, 1, 3, 0)))); //5
        waves.add(new Wave(new ArrayList<>(Arrays.asList(2, 0, 3, 0, 1, 0, 3, 0, 1)))); //6
        waves.add(new Wave(new ArrayList<>(Arrays.asList(2, 0, 1, 0, 3, 0)))); //7
        waves.add(new Wave(new ArrayList<>(Arrays.asList(2, 1, 0, 3, 1, 0, 3, 1)))); //8
        waves.add(new Wave(new ArrayList<>(Arrays.asList(2, 0, 0, 0, 2, 1, 3)))); //9
        waves.add(new Wave(new ArrayList<>(Arrays.asList(2, 0, 1, 0, 1, 1, 0, 1)))); //10
    }

    public List<Wave> getWaves() {
        return waves;
    }

    public boolean isTimeForNewEnemy() {
        return enemySpawnTick >= ENEMY_SPAWN_TICK_LIMIT;
    }

    public boolean isThereMoreEnemiesInWave() {
        return enemyIndex < waves.get(waveIndex).getEnemyList().size();
    }

    public boolean isThereMoveWaves() {
        return waveIndex + 1 < waves.size();
    }

    public void resetEnemyIndex() {
        enemyIndex = 0;
    }

    public int getWaveIndex() {
        return waveIndex;
    }

    public float getTimeLeft() {
        float ticksLeft = WAVE_TICK_LIMIT - waveTick;
        return ticksLeft / 60.0f;
    }

    public boolean isWaveTimerStarted() {
        return waveStartTimer;
    }

    public void reset() {
        waves.clear();
        createWaves();

        enemyIndex = 0;
        waveIndex = 0;
        waveStartTimer = false;
        waveTickTimerOver = false;
        waveTick = 0;
        enemySpawnTick = ENEMY_SPAWN_TICK_LIMIT;
    }
}
