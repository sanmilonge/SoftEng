# USE CASE: 9 Produce a Report of All Cities in a Region Ordered by Population

## CHARACTERISTIC INFORMATION

### Goal in Context
As a Data Analyst I want the system to produce a report of all cities in a region ordered by population from largest to smallest so that I can study urban concentration at the regional level.

### Scope
Organisation.

### Level
Primary task.

### Preconditions
The database contains population data for all cities within each region.

### Success End Condition
A report is available listing all cities in the specified region ordered by population from largest to smallest.

### Failed End Condition
No report is produced.

### Primary Actor
Data Analyst.

### Trigger
A request for city population information in a specific region is sent to the organisation.

## MAIN SUCCESS SCENARIO
1. Data Analyst requests a report of all cities in a specific region ordered by population.
2. System retrieves and sorts the population data for all cities within that region from largest to smallest.
3. System generates the report and provides it to the Data Analyst for review.

## EXTENSIONS
**Region not found or population data unavailable:**
1. System informs the Data Analyst that the region cannot be found or data is unavailable and no report can be generated.

## SUB-VARIATIONS
None.

## SCHEDULE
**DUE DATE:** Release 1.0
