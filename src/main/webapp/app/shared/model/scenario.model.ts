import { Moment } from 'moment';

export const enum ScenarioType {
    ImportSmall = 'ImportSmall',
    ImportBig = 'ImportBig',
    ReadSmall = 'ReadSmall',
    ReadBig = 'ReadBig'
}

export const enum Approach {
    REST = 'REST',
    WEBDRIVER = 'WEBDRIVER'
}

export interface IScenario {
    id?: number;
    name?: string;
    detail?: string;
    threadCount?: number;
    type?: ScenarioType;
    startDate?: Moment;
    endDate?: Moment;
    approach?: Approach;
}

export class Scenario implements IScenario {
    constructor(
        public id?: number,
        public name?: string,
        public detail?: string,
        public threadCount?: number,
        public type?: ScenarioType,
        public startDate?: Moment,
        public endDate?: Moment,
        public approach?: Approach
    ) {}
}
