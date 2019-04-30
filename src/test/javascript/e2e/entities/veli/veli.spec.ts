/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import VeliComponentsPage from './veli.page-object';
import { VeliDeleteDialog } from './veli.page-object';
import VeliUpdatePage from './veli-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Veli e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let veliUpdatePage: VeliUpdatePage;
  let veliComponentsPage: VeliComponentsPage;
  let veliDeleteDialog: VeliDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.waitUntilDisplayed();

    await signInPage.username.sendKeys('admin');
    await signInPage.password.sendKeys('admin');
    await signInPage.loginButton.click();
    await signInPage.waitUntilHidden();
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load Velis', async () => {
    await navBarPage.getEntityPage('veli');
    veliComponentsPage = new VeliComponentsPage();
    expect(await veliComponentsPage.getTitle().getText()).to.match(/Velis/);
  });

  it('should load create Veli page', async () => {
    await veliComponentsPage.clickOnCreateButton();
    veliUpdatePage = new VeliUpdatePage();
    expect(await veliUpdatePage.getPageTitle().getAttribute('id')).to.match(/ogrenciTakipSistemiApp.veli.home.createOrEditLabel/);
  });

  it('should create and save Velis', async () => {
    const nbButtonsBeforeCreate = await veliComponentsPage.countDeleteButtons();

    await veliUpdatePage.setAdInput('ad');
    expect(await veliUpdatePage.getAdInput()).to.match(/ad/);
    await veliUpdatePage.setSoyadInput('soyad');
    expect(await veliUpdatePage.getSoyadInput()).to.match(/soyad/);
    await veliUpdatePage.setAdresInput('adres');
    expect(await veliUpdatePage.getAdresInput()).to.match(/adres/);
    await veliUpdatePage.setMeslegiInput('meslegi');
    expect(await veliUpdatePage.getMeslegiInput()).to.match(/meslegi/);
    await veliUpdatePage.setEmailInput('email');
    expect(await veliUpdatePage.getEmailInput()).to.match(/email/);
    await veliUpdatePage.setTelefonCepInput('telefonCep');
    expect(await veliUpdatePage.getTelefonCepInput()).to.match(/telefonCep/);
    await veliUpdatePage.setTelefonSabitInput('telefonSabit');
    expect(await veliUpdatePage.getTelefonSabitInput()).to.match(/telefonSabit/);
    await waitUntilDisplayed(veliUpdatePage.getSaveButton());
    await veliUpdatePage.save();
    await waitUntilHidden(veliUpdatePage.getSaveButton());
    expect(await veliUpdatePage.getSaveButton().isPresent()).to.be.false;

    await veliComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await veliComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Veli', async () => {
    await veliComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await veliComponentsPage.countDeleteButtons();
    await veliComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    veliDeleteDialog = new VeliDeleteDialog();
    expect(await veliDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/ogrenciTakipSistemiApp.veli.delete.question/);
    await veliDeleteDialog.clickOnConfirmButton();

    await veliComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await veliComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
