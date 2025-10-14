# USE CASE: 14 Produce a Report of the Most Populated Cities in a Region

## CHARACTERISTIC INFORMATION

### Goal in Context
As a Policy Maker I want the system to produce a report of the most populated cities in a region, based on a number I specify, so that I can allocate funding to major regional hubs.

### Scope
Organisation.

### Level
Primary task.

### Preconditions
The database contains population data for all cities and their associated regions.

### Success End Condition
A report is available listing the specified number of the most populated cities within the selected region, ordered from largest to smallest population.

### Failed End Condition
No report is produced.

### Primary Actor
Policy Maker.

### Trigger
A request for a report of the most populated cities in a region is sent to the organisation.

## MAIN SUCCESS SCENARIO
1. Policy Maker selects a region and specifies the number of top populated cities to include in the report.
2. System retrieves and sorts city population data for that region from largest to smallest.
3. System generates the report and provides it to the Policy Maker for review.

## EXTENSIONS
**Invalid region or number specified:**
1. System prompts the Policy Maker to enter a valid region and/or number.

**Population data unavailable:**
1. System informs the Policy Maker that population data is unavailable and no report can be generated.

## SUB-VARIATIONS
None.

## SCHEDULE
**DUE DATE:** Release 1.0
