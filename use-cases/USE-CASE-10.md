# USE CASE: 10 Produce a Report of All Cities in a Country

## CHARACTERISTIC INFORMATION

### Goal in Context
As a Data Analyst I want the system to produce a report of all cities in a country ordered by population from largest to smallest so that I can analyse national population distribution.

### Scope
Organisation.

### Level
Primary task.

### Preconditions
The database contains population data for all cities and their associated countries.

### Success End Condition
A report is available listing all cities in the selected country ordered by population from largest to smallest.

### Failed End Condition
No report is produced.

### Primary Actor
Data Analyst.

### Trigger
A request for a report of all cities in a country is sent to the organisation.

## MAIN SUCCESS SCENARIO
1. Data Analyst selects a country and requests a population report for all cities within it.
2. System retrieves and sorts city population data for that country from largest to smallest.
3. System generates the report and provides it to the Data Analyst for review.

## EXTENSIONS
**Invalid country selected:**
1. System prompts the Data Analyst to select a valid country.

**Population data unavailable:**
1. System informs the Data Analyst that population data is unavailable and no report can be generated.

## SUB-VARIATIONS
None.

## SCHEDULE
**DUE DATE:** Release 1.0
