/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.bstguesser;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class TreeNode {
    private static final int SIZE = 60;
    private static final int MARGIN = 20;
    private int value, height;
    protected TreeNode left, right;
    private boolean showValue;
    private int x, y;
    private int color = Color.rgb(150, 150, 250);

    public TreeNode(int value) {
        this.value = value;
        this.height = 0;
        showValue = false;
        left = null;
        right = null;
    }

    public void insert(int valueToInsert) {
        if (valueToInsert < value)
        {
            if (left == null)
            {
                TreeNode newTreeNode = new TreeNode(valueToInsert);
                left = newTreeNode;
            }
            else
            {
                left.insert(valueToInsert);
            }
        }
        else if (valueToInsert > value) {
            if (right == null)
            {
                TreeNode newTreeNode = new TreeNode(valueToInsert);
                right = newTreeNode;
            }
            else
            {
                right.insert(valueToInsert);
            }
        }

        height = getHeight();

        Log.i("BST", "Value " + valueToInsert + " Height " + height);

        int balance = getBalance();

        Log.i("BST", "Balance " + balance);

        if (balance < -1){
            this.rotateLeft();
        }
        else if (balance > 1)
        {
            this.rotateRight();
        }
        else if (balance == 1 && right == null)
        {
            rotateRight();
        }
        else if (balance == -1 && left == null)
        {
            rotateLeft();
        }
    }

    private void rotateRight()
    {
        //Swap the values of the nodes
        int tempValue = value;
        value = left.value;
        left.left.value = tempValue;

        //Reform the connections
        right = left;
        left = right.left;
        right.left = null;

        height = getHeight();
    }

    private void rotateLeft()
    {
        //Swap the values of the nodes
        int tempValue = value;
        value = right.value;
        right.right.value = tempValue;

        //Reform the connections
        left = right;
        right = left.right;
        left.right = null;

        height = getHeight();
    }

    private int getBalance()
    {
        if (left == null && right == null)
        {
            return 0;
        }
        else if (left == null)
        {
            return 0 - right.height;
        }
        else if (right == null)
        {
            return left.height;
        }
        else
        {
            return left.height - right.height;
        }

    }

    private int getHeight()
    {
        if (left == null && right == null)
        {
            return 0;
        }
        else if (left == null)
        {
            return right.height++;
        }
        else if (right == null)
        {
            return left.height++;
        }
        else
        {
            return height = 1 + Math.max(left.height, right.height);
        }

    }


    public int getValue() {
        return value;
    }

    public void positionSelf(int x0, int x1, int y) {
        this.y = y;
        x = (x0 + x1) / 2;

        if(left != null) {
            left.positionSelf(x0, right == null ? x1 - 2 * MARGIN : x, y + SIZE + MARGIN);
        }
        if (right != null) {
            right.positionSelf(left == null ? x0 + 2 * MARGIN : x, x1, y + SIZE + MARGIN);
        }
    }

    public void draw(Canvas c) {
        Paint linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(3);
        linePaint.setColor(Color.GRAY);
        if (left != null)
            c.drawLine(x, y + SIZE/2, left.x, left.y + SIZE/2, linePaint);
        if (right != null)
            c.drawLine(x, y + SIZE/2, right.x, right.y + SIZE/2, linePaint);

        Paint fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(color);
        c.drawRect(x-SIZE/2, y, x+SIZE/2, y+SIZE, fillPaint);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(SIZE * 2/3);
        paint.setTextAlign(Paint.Align.CENTER);
        c.drawText(showValue ? String.valueOf(value) : "?", x, y + SIZE * 3/4, paint);

        if (height > 0) {
            Paint heightPaint = new Paint();
            heightPaint.setColor(Color.MAGENTA);
            heightPaint.setTextSize(SIZE * 2 / 3);
            heightPaint.setTextAlign(Paint.Align.LEFT);
            c.drawText(String.valueOf(height), x + SIZE / 2 + 10, y + SIZE * 3 / 4, heightPaint);
        }

        if (left != null)
            left.draw(c);
        if (right != null)
            right.draw(c);
    }

    public int click(float clickX, float clickY, int target) {
        int hit = -1;
        if (Math.abs(x - clickX) <= (SIZE / 2) && y <= clickY && clickY <= y + SIZE) {
            if (!showValue) {
                if (target != value) {
                    color = Color.RED;
                } else {
                    color = Color.GREEN;
                }
            }
            showValue = true;
            hit = value;
        }
        if (left != null && hit == -1)
            hit = left.click(clickX, clickY, target);
        if (right != null && hit == -1)
            hit = right.click(clickX, clickY, target);
        return hit;
    }

    public void invalidate() {
        color = Color.CYAN;
        showValue = true;
    }
}
