# Test assignment

## Abstract

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

## Solution

Seems that main problem of this test assignment is how to make atomic transfers.

First section of our API will be about wallet management:

- method `PUT /api/dev/wallets/{id}` allows us to create wallet with some initial balance in specified currency (in my implementation I will use [ISO 4217](http://www.iso.org/iso/home/standards/currency_codes.htm) three-letter codes)
- method `GET /api/dev/wallets/{id}` provides wallet details such as currency and balance

Assume we can transfer funds between wallets in the same currency without any fees. Otherwise we should use some currency conversion service (let's use [fixer API](http://fixer.io) for instance) and we also may want to specify some conversion fee. It may be implemented as a separate microservice (find out possible design [currency converter design|https://github.com/platbox/x-rates-java-test/blob/master/README.md] which I suggest senior-level applicants to implement).

So let's define some endpoints to handle wallet-to-wallet transfers:

- method `PUT /api/dev/transfers/{id}` executes transfer
- method `GET /api/dev/transfers/{id}` provides some transfer-specific information (status, timestamp etc.)

I want all entities to have a version to manage them with non-blocking and transaction-free storage. For test purposes I've put in-memory implementation, but the same technique may be used with SQL- and KV- storages (INSERT/UPDATE queries with entity version checking and almost the same trick with MongoDB for instance).

Current transfer execution may fail in a halfway and it have no restore mechanisms, so I'm thinking about transfer states and two-phase withdraws and charges.