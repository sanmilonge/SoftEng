# USE CASE: 23 Produce a Report Showing City and Non-City Population for Each Continent

## CHARACTERISTIC INFORMATION

### Goal in Context
As a Data Analyst I want the system to produce a report showing the city population and non-city population for each continent so that I can analyse continental urbanisation levels.

### Scope
Organisation.

### Level
Primary task.

### Preconditions
The database contains population data for all continents, including both city and non-city populations.

### Success End Condition
A report is available showing city and non-city population figures for each continent.

### Failed End Condition
No report is produced.

### Primary Actor
Data Analyst.

### Trigger
A request for a report showing city and non-city population data for each continent is sent to the organisation.

## MAIN SUCCESS SCENARIO
1. Data Analyst requests a report showing city and non-city population for each continent.
2. System retrieves and aggregates population data for cities and non-cities within each continent.
3. System generates the report and provides it to the Data Analyst for review.

## EXTENSIONS
**Population data incomplete or unavailable:**
1. System informs the Data Analyst that population data is incomplete or unavailable and no report can be generated.

## SUB-VARIATIONS
None.

## SCHEDULE
**DUE DATE:** Release 1.0
