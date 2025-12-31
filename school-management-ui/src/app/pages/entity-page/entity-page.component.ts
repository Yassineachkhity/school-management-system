import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CrudConfig } from '../../core/models';
import { EntityConfigService, EntityKey } from '../../core/entity-config.service';

@Component({
  selector: 'app-entity-page',
  templateUrl: './entity-page.component.html',
  styleUrl: './entity-page.component.scss'
})
export class EntityPageComponent implements OnInit {
  config: CrudConfig | null = null;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly entityConfig: EntityConfigService
  ) {}

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      const key = data['entity'] as EntityKey;
      if (!key) {
        this.config = null;
        return;
      }
      this.config = this.entityConfig.getConfig(key);
    });
  }
}
