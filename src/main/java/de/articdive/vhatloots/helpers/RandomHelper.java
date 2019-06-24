/*
 * VhatLoots
 * Copyright (C) 2019 Lukas Mansour
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.articdive.vhatloots.helpers;

import java.util.Random;

public class RandomHelper {
    private static final Random random = new Random();
    
    public static int rollForInt(int upper) {
        return random.nextInt(upper + 1); //+1 is needed to make it inclusive
    }
    
    public static int rollForInt(int lower, int upper) {
        return random.nextInt(upper + 1 - lower) + lower;
    }
    
    public static double rollForDouble(double upper) {
        return random.nextDouble() * upper;
    }
    
    public static double rollForDouble(int lower, int upper) {
        return random.nextInt(upper + 1 - lower) + lower;
    }

}
