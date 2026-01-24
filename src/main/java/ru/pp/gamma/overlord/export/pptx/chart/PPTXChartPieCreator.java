package ru.pp.gamma.overlord.export.pptx.chart;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xslf.usermodel.XSLFChart;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.presentation.entity.SlideField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PPTXChartPieCreator {

    public void create(SlideField field, XSLFChart chart) {
        try {
            // 0. Подготовка данных для "пирога"
            var categories = getData(field).entrySet().stream().toList();


            // 1. Получаем Excel страницу
            XSSFWorkbook workbook = chart.getWorkbook();
            XSSFSheet sheet = workbook.getSheetAt(0);

            // 2. Заполянем заголовок диаграммы. 1 строка, 2 ячейка
            sheet.getRow(0).getCell(1).setCellValue(getTitle(field));

            // 3. Записываем данные диаграммы
            for (int i = 0; i < categories.size(); i++) {
                int rowIndex = i + 1;

                if (sheet.getRow(rowIndex) == null) {
                    sheet.createRow(rowIndex);
                }

                var row = sheet.getRow(rowIndex);

                if (row.getCell(0) == null) {
                    row.createCell(0);
                }
                row.getCell(0).setCellValue(categories.get(i).getKey());

                if (row.getCell(1) == null) {
                    row.createCell(1);
                }
                row.getCell(1).setCellValue(categories.get(i).getValue());
            }

            // 4. Обновляем данные, которые использует диаграмма
            List<XDDFChartData> chartDataList = chart.getChartSeries();

            for (XDDFChartData chartData : chartDataList) {
                XDDFPieChartData pieData = (XDDFPieChartData) chartData;

                XDDFDataSource<String> categoriesData = XDDFDataSourcesFactory.fromStringCellRange(sheet,
                        new CellRangeAddress(1, categories.size(), 0, 0));

                XDDFNumericalDataSource<Double> valuesData = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
                        new CellRangeAddress(1, categories.size(), 1, 1));

                if (pieData.getSeriesCount() > 0) {
                    XDDFChartData.Series series = pieData.getSeries(0);
                    series.replaceData(categoriesData, valuesData);
                }

                chart.plot(pieData);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error when export to pptx pie chart", e);
        }
    }


    private String getTitle(SlideField field) {
        return field.getValue().get("title").asText();
    }

    private Map<String, Double> getData(SlideField field) {
        Map<String, Double> map = new HashMap<>();

        int i = 0;
        JsonNode data = field.getValue().get("data");
        while (data.get(i) != null) {
            JsonNode pair = data.get(i);
            map.put(pair.get("label").asText(), pair.get("value").asDouble());
            i++;
        }

        return map;
    }

}
