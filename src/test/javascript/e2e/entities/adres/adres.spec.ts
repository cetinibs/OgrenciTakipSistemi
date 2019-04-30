/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import AdresComponentsPage from './adres.page-object';
import { AdresDeleteDialog } from './adres.page-object';
import AdresUpdatePage from './adres-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Adres e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let adresUpdatePage: AdresUpdatePage;
  let adresComponentsPage: AdresComponentsPage;
  let adresDeleteDialog: AdresDeleteDialog;

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

  it('should load Adres', async () => {
    await navBarPage.getEntityPage('adres');
    adresComponentsPage = new AdresComponentsPage();
    expect(await adresComponentsPage.getTitle().getText()).to.match(/Adres/);
  });

  it('should load create Adres page', async () => {
    await adresComponentsPage.clickOnCreateButton();
    adresUpdatePage = new AdresUpdatePage();
    expect(await adresUpdatePage.getPageTitle().getAttribute('id')).to.match(/ogrenciTakipSistemiApp.adres.home.createOrEditLabel/);
  });

  it('should create and save Adres', async () => {
    const nbButtonsBeforeCreate = await adresComponentsPage.countDeleteButtons();

    await adresUpdatePage.setEvAdresInput('evAdres');
    expect(await adresUpdatePage.getEvAdresInput()).to.match(/evAdres/);
    await adresUpdatePage.setIsAdresInput('isAdres');
    expect(await adresUpdatePage.getIsAdresInput()).to.match(/isAdres/);
    await adresUpdatePage.setEmailInput('email');
    expect(await adresUpdatePage.getEmailInput()).to.match(/email/);
    await adresUpdatePage.setTelefonCepInput('telefonCep');
    expect(await adresUpdatePage.getTelefonCepInput()).to.match(/telefonCep/);
    await adresUpdatePage.setTelefonSabitInput('telefonSabit');
    expect(await adresUpdatePage.getTelefonSabitInput()).to.match(/telefonSabit/);
    await adresUpdatePage.musteriSelectLastOption();
    await waitUntilDisplayed(adresUpdatePage.getSaveButton());
    await adresUpdatePage.save();
    await waitUntilHidden(adresUpdatePage.getSaveButton());
    expect(await adresUpdatePage.getSaveButton().isPresent()).to.be.false;

    await adresComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await adresComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Adres', async () => {
    await adresComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await adresComponentsPage.countDeleteButtons();
    await adresComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    adresDeleteDialog = new AdresDeleteDialog();
    expect(await adresDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/ogrenciTakipSistemiApp.adres.delete.question/);
    await adresDeleteDialog.clickOnConfirmButton();

    await adresComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await adresComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
