import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IScenario } from 'app/shared/model/scenario.model';
import { ScenarioService } from './scenario.service';

@Component({
    selector: 'jhi-scenario-update',
    templateUrl: './scenario-update.component.html'
})
export class ScenarioUpdateComponent implements OnInit {
    scenario: IScenario;
    isSaving: boolean;
    startDate: string;
    endDate: string;

    constructor(protected scenarioService: ScenarioService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ scenario }) => {
            this.scenario = scenario;
            this.startDate = this.scenario.startDate != null ? this.scenario.startDate.format(DATE_TIME_FORMAT) : null;
            this.endDate = this.scenario.endDate != null ? this.scenario.endDate.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.scenario.startDate = this.startDate != null ? moment(this.startDate, DATE_TIME_FORMAT) : null;
        this.scenario.endDate = this.endDate != null ? moment(this.endDate, DATE_TIME_FORMAT) : null;
        if (this.scenario.id !== undefined) {
            this.subscribeToSaveResponse(this.scenarioService.update(this.scenario));
        } else {
            this.subscribeToSaveResponse(this.scenarioService.create(this.scenario));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IScenario>>) {
        result.subscribe((res: HttpResponse<IScenario>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
