# USE CASE: 1 Produce a Report on the Population of All Countries in the World

## CHARACTERISTIC INFORMATION

### Goal in Context

As a Data Analyst I want the system to produce a report of all the countries in the world organised by largest population to smallest so that I can compare global population sizes.

### Scope

Organisation

### Level

Primary task.

### Preconditions

The database contains population data for all countries.

### Success End Condition

A report is available listing all countries ordered by population from largest to smallest.

### Failed End Condition

No report is produced.

### Primary Actor

Data Analyst.

### Trigger

A request for country population information is sent to the organisation.

## MAIN SUCCESS SCENARIO

1. Data Analyst requests a population report for all countries.
2. System retrieves and sorts data from largest to smallest population.
3. System generates the report and provides it to the Data Analyst for review.

## EXTENSIONS

**Population data unavailable:**:
    1. Data Analyst informs organisation data is unavailable and no report can be generated.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 1.0