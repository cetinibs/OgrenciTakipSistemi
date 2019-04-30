import { element, by, ElementFinder } from 'protractor';

export default class AdresUpdatePage {
  pageTitle: ElementFinder = element(by.id('ogrenciTakipSistemiApp.adres.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  evAdresInput: ElementFinder = element(by.css('input#adres-evAdres'));
  isAdresInput: ElementFinder = element(by.css('input#adres-isAdres'));
  emailInput: ElementFinder = element(by.css('input#adres-email'));
  telefonCepInput: ElementFinder = element(by.css('input#adres-telefonCep'));
  telefonSabitInput: ElementFinder = element(by.css('input#adres-telefonSabit'));
  musteriSelect: ElementFinder = element(by.css('select#adres-musteri'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setEvAdresInput(evAdres) {
    await this.evAdresInput.sendKeys(evAdres);
  }

  async getEvAdresInput() {
    return this.evAdresInput.getAttribute('value');
  }

  async setIsAdresInput(isAdres) {
    await this.isAdresInput.sendKeys(isAdres);
  }

  async getIsAdresInput() {
    return this.isAdresInput.getAttribute('value');
  }

  async setEmailInput(email) {
    await this.emailInput.sendKeys(email);
  }

  async getEmailInput() {
    return this.emailInput.getAttribute('value');
  }

  async setTelefonCepInput(telefonCep) {
    await this.telefonCepInput.sendKeys(telefonCep);
  }

  async getTelefonCepInput() {
    return this.telefonCepInput.getAttribute('value');
  }

  async setTelefonSabitInput(telefonSabit) {
    await this.telefonSabitInput.sendKeys(telefonSabit);
  }

  async getTelefonSabitInput() {
    return this.telefonSabitInput.getAttribute('value');
  }

  async musteriSelectLastOption() {
    await this.musteriSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async musteriSelectOption(option) {
    await this.musteriSelect.sendKeys(option);
  }

  getMusteriSelect() {
    return this.musteriSelect;
  }

  async getMusteriSelectedOption() {
    return this.musteriSelect.element(by.css('option:checked')).getText();
  }

  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton() {
    return this.saveButton;
  }
}
