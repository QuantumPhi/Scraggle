package com.onalant;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Board {
    public static final String API_URL = "API_URL";

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
                values[i][j].tile.setRegionWidth((int)(0.75f * values[i][j].tile.getWidth()));
                values[i][j].tile.setRegionHeight((int)(0.75f * values[i][j].tile.getHeight()));
            }
        }
    }

    public void render(SpriteBatch batch) {
        for(int i = 0; i < values.length; i++)
            for(int j = 0; j < values[0].length; j++)
                batch.draw(values[i][j].tile, 90+215*i, 90+215*j);
    }

    public Tile onTile(int x, int y) {
        return null;
    }

    public static class Tile {
        public Sprite tile = new Sprite(new Texture("tile.png"));

        public char c;

        public int x;
        public int y;

        public Tile(char c) {
            this.c = c;
        }
    }
}
