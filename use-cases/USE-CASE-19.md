# USE CASE: 19 Produce a Report of All Capital Cities in a Region

## CHARACTERISTIC INFORMATION

### Goal in Context
As a Data Analyst I want the system to produce a report of all capital cities in a region ordered by population from largest to smallest so that I can assess regional governance populations.

### Scope
Organisation.

### Level
Primary task.

### Preconditions
The database contains population data for all capital cities and their associated regions.

### Success End Condition
A report is available listing all capital cities in the selected region ordered by population from largest to smallest.

### Failed End Condition
No report is produced.

### Primary Actor
Data Analyst.

### Trigger
A request for a report of all capital cities in a region is sent to the organisation.

## MAIN SUCCESS SCENARIO
1. Data Analyst selects a region and requests a population report for all capital cities within it.
2. System retrieves and sorts capital city population data for that region from largest to smallest.
3. System generates the report and provides it to the Data Analyst for review.

## EXTENSIONS
**Invalid region selected:**
1. System prompts the Data Analyst to select a valid region.

**Population data unavailable:**
1. System informs the Data Analyst that population data is unavailable and no report can be generated.

## SUB-VARIATIONS
None.

## SCHEDULE
**DUE DATE:** Release 1.0
