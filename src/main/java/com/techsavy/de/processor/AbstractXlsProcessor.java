package com.techsavy.de.processor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import com.techsavy.de.domain.PrerequisiteResponse;
import com.techsavy.de.domain.RuleResponse;
import com.techsavy.de.domain.XlsRuleData;

public abstract class AbstractXlsProcessor extends BaseProcessor {
	private static final Logger log = LogManager.getLogger();

	public AbstractXlsProcessor() {
		buildPrerequistes();
		buildRules();
	}

	private static List<XlsRuleData> rulesList = new ArrayList<>();

	protected void readRulesFile() {
		String xlsFileName = getRulesFileName();
		log.info("Loading rules from file in classpath {}", xlsFileName);
		ExpressionParser parser = new SpelExpressionParser();
		try (InputStream inputStream = AbstractXlsProcessor.class.getClassLoader().getResourceAsStream(xlsFileName);
				XSSFWorkbook myWorkBook = new XSSFWorkbook(inputStream);) {
			XSSFSheet mySheet = myWorkBook.getSheetAt(0);
			Iterator<Row> rowIterator = mySheet.iterator(); // Traversing over each row of XLSX file
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next(); // For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				XlsRuleData ruleData = new XlsRuleData();
				ruleData.setRuleName(cellIterator.next().getStringCellValue());
				ruleData.setDecision(cellIterator.next().getStringCellValue());
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					System.out.print(cell.getStringCellValue() + "\t");
					Expression expression = parser.parseExpression(cell.getStringCellValue());
					ruleData.getExpressions().add(expression);
					rulesList.add(ruleData);
				}
				// System.out.println("");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public abstract String getRulesFileName();

	@Override
	protected void buildPrerequistes() {
		log.debug("Processing XlsProcecssor:buildPrerequistes");
		if (rulesList == null || rulesList.size() == 0) {
			readRulesFile();
		}
		prerequisites.add((decisionEngineRequest) -> {
			PrerequisiteResponse prerequisiteResponse = PrerequisiteResponse.getInstance("XlsProcessor:Prerequiste1");
			prerequisiteResponse.setPassed(true);
			return prerequisiteResponse;
		});
	}

	@Override
	protected void buildRules() {
		rulesList.forEach(rule -> {
			rules.add((decisionEngineRequest, processorResponse) -> {
				rule.getExpressions().forEach(expression -> {
					RuleResponse ruleResponse = RuleResponse.getInstance(rule.getRuleName() + "." + expression.getExpressionString());
					processorResponse.getDecisionArrivalSteps().put(expression.getExpressionString(),
							expression.getValue(decisionEngineRequest, Boolean.class).toString());
					ruleResponse.getAudit().setEndTime(System.currentTimeMillis());
					processorResponse.addRuleResponse(ruleResponse);
				});
				return processorResponse.getRuleResponses().get(0);
			});
		});
	}
}
