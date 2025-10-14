# USE CASE: 18 Produce a Report of All Capital Cities in a Continent

## CHARACTERISTIC INFORMATION

### Goal in Context
As a Data Analyst I want the system to produce a report of all capital cities in a continent ordered by population from largest to smallest so that I can evaluate continental administrative trends.

### Scope
Organisation.

### Level
Primary task.

### Preconditions
The database contains population data for all capital cities and their associated continents.

### Success End Condition
A report is available listing all capital cities in the selected continent ordered by population from largest to smallest.

### Failed End Condition
No report is produced.

### Primary Actor
Data Analyst.

### Trigger
A request for a report of all capital cities in a continent is sent to the organisation.

## MAIN SUCCESS SCENARIO
1. Data Analyst selects a continent and requests a population report for all capital cities within it.
2. System retrieves and sorts capital city population data for that continent from largest to smallest.
3. System generates the report and provides it to the Data Analyst for review.

## EXTENSIONS
**Invalid continent selected:**
1. System prompts the Data Analyst to select a valid continent.

**Population data unavailable:**
1. System informs the Data Analyst that population data is unavailable and no report can be generated.

## SUB-VARIATIONS
None.

## SCHEDULE
**DUE DATE:** Release 1.0
