package com.onalant;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Board {
    public static final String API_URL = "API_URL";
    public static final Color INACTIVE = new Color(0, 128, 128, 255);
    public static final Color ACTIVE = new Color(255, 255, 255, 255);
    public static final Color RIGHT = new Color(46, 204, 113, 255);
    public static final Color WRONG = new Color(231, 76, 60, 255);

    public Tile[][] values;

    public static Board load() {
        Board newBoard = null;
        URL url = null;
        String str = null;

        try {
            url = new URL(API_URL);
        } catch (MalformedURLException e) {
            Gdx.app.error("GAME", "Malformed URL", e);
        }

        try {
            str = (new BufferedReader(new InputStreamReader(url.openStream()))).readLine();
        } catch (IOException e) {
            Gdx.app.error("GAME", "Error opening InputStream", e);
        }

        newBoard = new Board(str);

        return newBoard;
    }

    public Board(String board) {
        values = new Tile[4][4];
        if(board.length() != 16)
            return;
        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                values[i][j] = new Tile(board.charAt(i * 4 + j));
                values[i][j].x = 356 + 255 * i;
                values[i][j].y = 30 + 255 * j;
            }
        }
    }

    public void render(ShapeRenderer renderer) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        for(int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                renderer.setColor(values[i][j].isActive ? ACTIVE : INACTIVE);
                renderer.rect(values[i][j].x, values[i][j].y, values[i][j].width, values[i][j].height);
            }
        }
        renderer.end();
    }

    public Tile onTile(int x, int y) {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                if(values[i][j].onTile(x, y))
                    return values[i][3-j];
        return null;
    }

    public void reset() {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                values[i][j].isActive = false;
    }

    public void resetWrong() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                values[i][j].wrong = 0;
    }

    public static class Tile {
        public static String current = "";

        public Rectangle tile = new Rectangle();
        public boolean preliminary, isActive;

        //0 -> neutral; 1 -> right; 2 -> wrong
        public int wrong;

        public char c;

        public int x, y,
                width = 240,
                height = 240;

        public Tile(char c) {
            this.c = c;
        }

        public boolean onTile(int x, int y) {
            return Math.hypot(this.x - x + 120, this.y - y + 120) <= 120;
        }
    }
}
