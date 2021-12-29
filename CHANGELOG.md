Changelog
=========
2.3.0
-----------------
- Added new variable to HyperwalletUser class
- Added new variable to HyperwalletUserListPagination class
- Added missing webhooks

2.2.6
-----------------
- Query parameters added for the methods 'List Bank Account', 'List Bank Card', 'List Paypal Account', 'List Venmo Account', 'List Prepaid Card', 'List Paper Check', 'List Payment', 'List Transfer Method' 

2.2.5
-----------------
- Fix for multipart Upload

2.2.3
-----------------
- Amount field data type changed from Double to String

2.2.2
-----------------
- Reject reasons added to Multipart document upload functionality

2.2.1
-----------------
- Modified Multipart upload 

2.2.0
-----------------
- Added Upload Multipart form-data (removed Jersey dependency)
- Added Business stakeholder status transitions (get, list)
- Added Bank Account status transitions (get)


2.1.0
-----------------
- Added Business Stakeholders status transitions
- Added User status transitions (activate, deactivate, freeze, lock, preactivate)
- Foreign exchanges in Transfer Refunds

2.0.1
-----------------
- Updated endpoints to V4 Rest services.
- Added links to objects for pagination
- Added Business Stakeholders 
- Added filters to endpoints
- Added Venmo accounts to the SDK
- Added Multipart document upload functionality to Users and Stakeholders

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
