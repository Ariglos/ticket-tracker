import {Directive, OnDestroy} from "@angular/core";
import {Subject} from "rxjs";

@Directive({
  standalone: true,
})
export class DestroyedDirective implements OnDestroy {
  destroyed$ = new Subject<void>();

  ngOnDestroy() {
    this.destroyed$.next();
    this.destroyed$.complete();
  }
}
