# USE CASE: 4 Produce a Report of the Most Populated Countries in the World

## CHARACTERISTIC INFORMATION

### Goal in Context
As a Policy Maker I want the system to produce a report of the most populated countries in the world, based on a number I specify, so that I can focus analysis on key global nations.

### Scope
Organisation.

### Level
Primary task.

### Preconditions
The database contains population data for all countries.

### Success End Condition
A report is available listing the specified number of the most populated countries in the world, ordered from largest to smallest population.

### Failed End Condition
No report is produced.

### Primary Actor
Policy Maker.

### Trigger
A request for a report on the most populated countries in the world is sent to the organisation.

## MAIN SUCCESS SCENARIO
1. Policy Maker specifies the number of top populated countries to include in the report.
2. System retrieves and sorts data by population from largest to smallest.
3. System generates a report listing the specified number of countries and provides it to the Policy Maker for review.

## EXTENSIONS
**Invalid number specified:**
1. System prompts the Policy Maker to enter a valid number.

**Population data unavailable:**
1. System informs the Policy Maker that population data is unavailable and no report can be generated.

## SUB-VARIATIONS
None.

## SCHEDULE
**DUE DATE:** Release 1.0
