import {FormGroup} from "@angular/forms";

export const markFromGroupTouched = (formGroup: FormGroup) => {
  (Object as any).values(formGroup.controls).forEach((control) => {
    control.markAsTouched();
    if (control.controls) {
      markFromGroupTouched(control);
    }
  });
};
