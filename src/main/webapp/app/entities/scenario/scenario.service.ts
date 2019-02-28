import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IScenario } from 'app/shared/model/scenario.model';

type EntityResponseType = HttpResponse<IScenario>;
type EntityArrayResponseType = HttpResponse<IScenario[]>;

@Injectable({ providedIn: 'root' })
export class ScenarioService {
    public resourceUrl = SERVER_API_URL + 'api/scenarios';

    constructor(protected http: HttpClient) {}

    create(scenario: IScenario): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(scenario);
        return this.http
            .post<IScenario>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(scenario: IScenario): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(scenario);
        return this.http
            .put<IScenario>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IScenario>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IScenario[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(scenario: IScenario): IScenario {
        const copy: IScenario = Object.assign({}, scenario, {
            startDate: scenario.startDate != null && scenario.startDate.isValid() ? scenario.startDate.toJSON() : null,
            endDate: scenario.endDate != null && scenario.endDate.isValid() ? scenario.endDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.startDate = res.body.startDate != null ? moment(res.body.startDate) : null;
            res.body.endDate = res.body.endDate != null ? moment(res.body.endDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((scenario: IScenario) => {
                scenario.startDate = scenario.startDate != null ? moment(scenario.startDate) : null;
                scenario.endDate = scenario.endDate != null ? moment(scenario.endDate) : null;
            });
        }
        return res;
    }
}
