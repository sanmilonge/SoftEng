# USE CASE: 3 Produce a Report on the Population of Countries in a Specific Region

## CHARACTERISTIC INFORMATION

### Goal in Context
As a Data Analyst I want the system to produce a report of all countries in a specific region ordered by population from largest to smallest so that I can identify the most and least populated regions.

### Scope
Organisation

### Level
Primary task.

### Preconditions
The database contains population data for all countries, including their associated regions.

### Success End Condition
A report is available listing all countries in the selected region ordered by population from largest to smallest.

### Failed End Condition
No report is produced.

### Primary Actor
Data Analyst.

### Trigger
A request for population information of countries within a specific region is sent to the organisation.

## MAIN SUCCESS SCENARIO
1. Data Analyst requests a population report for countries in a specific region.
2. System retrieves and sorts data from largest to smallest population.
3. System generates the report and provides it to the Data Analyst for review.

## EXTENSIONS
**Region data unavailable:**
1. Data Analyst informs organisation that data for the selected region is unavailable and no report can be generated.

## SUB-VARIATIONS
None.

## SCHEDULE
**DUE DATE:** Release 1.0
