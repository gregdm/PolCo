'use strict';

angular.module('polcoApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
