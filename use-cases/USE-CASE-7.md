# USE CASE: 7 Produce a Report of All Cities in the World

## CHARACTERISTIC INFORMATION

### Goal in Context
As a Data Analyst I want the system to produce a report of all cities in the world ordered by population from largest to smallest so that I can understand global urbanisation patterns.

### Scope
Organisation.

### Level
Primary task.

### Preconditions
The database contains population data for all cities in the world.

### Success End Condition
A report is available listing all cities in the world ordered by population from largest to smallest.

### Failed End Condition
No report is produced.

### Primary Actor
Data Analyst.

### Trigger
A request for city population information is sent to the organisation.

## MAIN SUCCESS SCENARIO
1. Data Analyst requests a population report for all cities in the world.
2. System retrieves and sorts city population data from largest to smallest.
3. System generates the report and provides it to the Data Analyst for review.

## EXTENSIONS
**Population data unavailable:**
1. System informs the Data Analyst that population data is unavailable and no report can be generated.

## SUB-VARIATIONS
None.

## SCHEDULE
**DUE DATE:** Release 1.0
