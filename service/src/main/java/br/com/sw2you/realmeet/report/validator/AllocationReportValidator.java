package br.com.sw2you.realmeet.report.validator;

import static br.com.sw2you.realmeet.util.Constants.ALLOCATION_REPORT_MAX_MONTHS_INTERVAL;
import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;
import static br.com.sw2you.realmeet.validator.ValidatorUtils.validateRequired;

import br.com.sw2you.realmeet.report.model.AbstractReportData;
import br.com.sw2you.realmeet.report.model.AllocationReportData;
import br.com.sw2you.realmeet.validator.ValidationErrors;
import java.time.Period;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AllocationReportValidator extends AbstractReportValidator {
    private final int maxMonthInterval;

    public AllocationReportValidator(@Value(ALLOCATION_REPORT_MAX_MONTHS_INTERVAL) int maxMonthInterval) {
        this.maxMonthInterval = maxMonthInterval;
    }

    @Override
    protected void validate(AbstractReportData reportData, ValidationErrors validationsErrors) {
        var allocationReportData = (AllocationReportData) reportData;

        validateRequired(allocationReportData.getDateFrom(), DATE_FROM, validationsErrors);
        validateRequired(allocationReportData.getDateTo(), DATE_TO, validationsErrors);

        var isNonNull = allocationReportData.getDateFrom() != null && allocationReportData.getDateTo() != null;

        if (isNonNull && allocationReportData.getDateFrom().isAfter(allocationReportData.getDateTo())) {
            validationsErrors.add(DATE_FROM, DATE_FROM + INCONSISTENT);
        } else if (
            isNonNull &&
            Period.between(allocationReportData.getDateFrom(), allocationReportData.getDateTo()).getMonths() >
            maxMonthInterval
        ) {
            validationsErrors.add(DATE_TO, DATE_TO + EXCEEDS_INTERVAL);
        }
    }
}
