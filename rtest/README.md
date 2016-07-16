Test assignment
===============

# Abstract

Design and implement a RESTful API (including data model and the backing implementation) for money transfers between internal users/accounts.

Explicit requirements:

1. keep it simple and to the point (e.g. no need to implement any authentication, assume the APi is invoked by another internal system/service)
2. use whatever frameworks/libraries you like (except Spring, sorry!) but don't forget about the requirement #1
3. the datastore should run in­memory for the sake of this test
4. the final result should be executable as a standalone program (should not require a pre­installed container/server)
5. demonstrate with tests that the API works as expected

Implicit requirements:

1. the code produced by you is expected to be of good quality.
2. there are no detailed requirements, use common sense.

Please put your work on github or bitbucket.

# Solution

Seems that main problem of this test assignment is how to make atomic transfers.

First section of our API will be about user management:

- method `PUT /api/dev/users/{id}` allows us to create new user
- method `GET /api/dev/users/{id}` provides some user info
- method `DELETE /api/dev/users/{id}` blocks existing user

Next section will serve users wallets:

- method `PUT /api/dev/users/{id}/wallets/{currency}` for wallet creation with some initial balance
- method `GET /api/dev/users/{id}/wallets/{currency}` for wallet details
- method `DELETE /api/dev/users/{id}/wallets/{currency}` for wallet blocking

Assume that every user can store his funds in any currency (in my implementation I will use [ISO 4217](http://www.iso.org/iso/home/standards/currency_codes.htm) three-letter codes in lower case).

Assume also we can transfer funds between wallets in the same currency without any fees. Otherwise we should use some currency conversion service (let's use [fixer API](http://fixer.io) for instance) and we also may want to specify some conversion fee.

Finally let's define some endpoints to handle wallet-to-wallet transfers:

- method `PUT /api/dev/transfers/{id}` initiates transfer
- method `GET /api/dev/transfers/{id}` provides some transfer-specific information (status, timestamp etc.)