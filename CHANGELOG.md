Changelog
=========
1.7.1 
-----------------
- Added Reject reasons for Multipart upload document functionality

1.7.0
-----------------
- Added custom headers
- Added multipart upload functionality without Jersey dependency
- Added TransferStatusTransitions (get, list)
- Added Tranfer Refund methods (create, get, list)
- Added updatePaypalAccount method
- Added methods - getBankAccountStatusTransition(), listTransferMethods

1.6.1
-----------------
- Added 'processingTime' to BankCards
- Added 'expiresOn' to Payments

1.6.0
-----------------
- Added Status Transitions for User (activate, deactivate, preactivate, freeze, lock)
- Added Status transition for Verification Status for User
- Added Foreign Exchanges to Transfer Refund

1.5.0
------------------
- Added Venmo accounts

1.4.2
------------------
- Added status param to HyperwalletPayment

1.4.1
-------------------
- Added support for new businessType field values. 

1.4.0
-------------------
- Fix TypeError thrown when response status is 204 No content
- Add CVV field to the SDK
- Remove Relationship field from Server SDK
- Add Business Operating Name Field to User
- Add PayPal account status transitions

1.3.0 (2019-01-28)
-------------------
- Added field "VerificationStatus" to User
- Client-token endpoint renamed to authentication-token

0.5.1 (2019-01-17)
-------------------
- FIX: Resolved issue with restricted "Accept" & "Content-Type" headers to support only "application/json" or "application/jose+json"

0.5.0 (2018-12-20)
-------------------

- Restricted “Accept” & “Content-Type” headers to support only “application/json” or “application/jose+json”
- Added Related resources “relatedResources” in error representation is added
- Added Authentication token endpoint

0.4.0 (2018-10-11)
-------------------

- Added Layer7 encryption

0.3.0 (2018-10-10)
-------------------

- Added PayPal endpoint
- Added Transfer endpoint
- Added Payment Status Transitions endpoint
- Added Update Prepaid Card endpoint
- Added Paper Checks endpoint
- Added bank card endpoint

0.2.0 (2017-03-07)
------------------

- Added list program account receipt endpoint
- Added list user receipt endpoint
- Added list prepaid card receipt endpoint
- Fixed the payment representation (renamed description to notes)

0.1.0 (2016-06-30)
------------------

- Initial release
