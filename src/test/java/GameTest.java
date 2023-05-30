import com.go.Board;
import com.go.Game;
import com.go.Stone;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GameTest {

    @TempDir
    File tempDir;

    @Test
    void testSaveArrayToFile() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Board board = new Board();
        Game game = new Game(board);

        // Создание временного файла
        File tempFile = new File(tempDir, "testFile.txt");

        // Создание двумерного массива
        Stone[][] testArray = new Stone[2][3];
        testArray[0][0] = new Stone(Game.PlayerColor.BLACK, 0, 0);
        testArray[0][1] = new Stone(Game.PlayerColor.WHITE, 0, 1);
        testArray[1][2] = new Stone(Game.PlayerColor.BLACK, 1, 2);

        // Использование приватного метода saveArrayToFile посредством рефлексии
        Method saveArrayToFileMethod = Game.class.getDeclaredMethod("saveArrayToFile", File.class, Stone[][].class);
        saveArrayToFileMethod.setAccessible(true);
        saveArrayToFileMethod.invoke(game, tempFile, testArray);

        // Чтение данных из сохраненного файла
        try (BufferedReader br = new BufferedReader(new FileReader(tempFile))) {
            int expectedRowCount = 2;
            int expectedColumnCount = 3;
            assertEquals(expectedRowCount, Integer.parseInt(br.readLine()));
            assertEquals(expectedColumnCount, Integer.parseInt(br.readLine()));

            // Проверка массива
            for (int i = 0; i < expectedRowCount; i++) {
                String[] line = br.readLine().split(" ");
                for (int j = 0; j < expectedColumnCount; j++) {
                    if (!line[j].equals("null")) {
                        Game.PlayerColor expectedColor = Game.PlayerColor.valueOf(line[j]);
                        assertEquals(expectedColor, testArray[i][j].playerColor());
                    } else {
                        assertNull(testArray[i][j]);
                    }
                }
            }
        }
    }
}
