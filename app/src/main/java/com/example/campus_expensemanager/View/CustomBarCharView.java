package com.example.campus_expensemanager.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.campus_expensemanager.entities.Expense;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomBarCharView extends View {

    private List<Expense> expenses; // Dữ liệu chi tiêu
    private Paint paint;

    public CustomBarCharView(Context context) {
        super(context);
        init();
    }

    public CustomBarCharView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    // Phương thức để set dữ liệu chi tiêu
    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        invalidate(); // Gọi lại onDraw() để vẽ lại biểu đồ
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (expenses == null || expenses.size() == 0) return;

        // Bước 1: Nhóm các chi tiêu theo category và tính tổng chi tiêu cho mỗi category
        Map<String, Float> categoryTotals = new HashMap<>();
        for (Expense expense : expenses) {
            categoryTotals.put(expense.getCategory(), categoryTotals.getOrDefault(expense.getCategory(), 0f) + expense.getAmount());
        }

        // Tính tổng chi tiêu
        float total = 0;
        for (float amount : categoryTotals.values()) {
            total += amount;
        }

        // Chuyển các mục chi tiêu đã nhóm thành một danh sách
        List<Map.Entry<String, Float>> groupedExpenses = new ArrayList<>(categoryTotals.entrySet());
        // Sắp xếp danh sách theo category
        Collections.sort(groupedExpenses, new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> e1, Map.Entry<String, Float> e2) {
                return e1.getKey().compareTo(e2.getKey());
            }
        });

        // Vẽ các phần của biểu đồ tròn
        float startAngle = 0;
        int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA}; // Màu sắc cho từng hạng mục
        int colorIndex = 0;

        for (Map.Entry<String, Float> entry : groupedExpenses) {
            float sweepAngle = (entry.getValue() / total) * 360;

            paint.setColor(colors[colorIndex % colors.length]); // Lấy màu tương ứng
            colorIndex++;

            canvas.drawArc(200, 200, getWidth() - 200, getHeight() - 200, startAngle, sweepAngle, true, paint);

            startAngle += sweepAngle;
        }

        // Vẽ chú thích cho từng hạng mục
        float legendY = getHeight() - 150; // Khoảng cách giữa biểu đồ và chú thích
        paint.setTextSize(40);
        paint.setColor(Color.BLACK);

        colorIndex = 0;
        for (Map.Entry<String, Float> entry : groupedExpenses) {
            paint.setColor(colors[colorIndex % colors.length]);
            canvas.drawRect(50, legendY - 30, 100, legendY, paint);

            paint.setColor(Color.BLACK);
            canvas.drawText(entry.getKey() + " (" + entry.getValue() + ")", 120, legendY, paint);

            legendY += 50;
            colorIndex++;
        }
    }
}

