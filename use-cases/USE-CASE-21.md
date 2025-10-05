# USE CASE: 21 Produce a Report of the Most Populated Capital Cities in a Continent

## CHARACTERISTIC INFORMATION

### Goal in Context
As a Policy Maker I want the system to produce a report of the most populated capital cities in a continent, based on a number I specify, so that I can guide political and economic planning.

### Scope
Organisation.

### Level
Primary task.

### Preconditions
The database contains population data for all capital cities and their associated continents.

### Success End Condition
A report is available listing the specified number of the most populated capital cities within the selected continent, ordered from largest to smallest population.

### Failed End Condition
No report is produced.

### Primary Actor
Policy Maker.

### Trigger
A request for a report of the most populated capital cities in a continent is sent to the organisation.

## MAIN SUCCESS SCENARIO
1. Policy Maker selects a continent and specifies the number of top populated capital cities to include in the report.
2. System retrieves and sorts capital city population data for that continent from largest to smallest.
3. System generates the report and provides it to the Policy Maker for review.

## EXTENSIONS
**Invalid continent or number specified:**
1. System prompts the Policy Maker to enter a valid continent and/or number.

**Population data unavailable:**
1. System informs the Policy Maker that population data is unavailable and no report can be generated.

## SUB-VARIATIONS
None.

## SCHEDULE
**DUE DATE:** Release 1.0
